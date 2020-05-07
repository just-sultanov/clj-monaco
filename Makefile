.EXPORT_ALL_VARIABLES:
.DEFAULT_GOAL = help
.PHONY: help pom.xml

SHELL = bash
OS = $(shell uname -a)
OS_NAME = $(shell uname -s | tr A-Z a-z)

include .env


ifndef GIT_SHA
  GIT_SHA = $(shell git rev-parse HEAD)
endif


ifeq ($(OS_NAME), darwin)
    # requirements: brew install coreutils
    TIMESTAMP = $(shell gdate --utc +%FT%T.%3NZ)
endif


ifeq ($(OS_NAME), linux)
    TIMESTAMP = $(shell date --utc +%FT%T.%3NZ)
endif



LINES=--------------------------------------------------------------
PRIMARY_TEXT_COLOR=\033[36m
NORMAL_TEXT_COLOR=\033[0m


define pprint
	@echo -e "$(PRIMARY_TEXT_COLOR)"
	@echo -e "$(LINES)"
	@echo -e $(1)
	@echo -e "$(LINES)"
	@echo -e "$(NORMAL_TEXT_COLOR)"
endef


help: ## Show help
	@awk 'BEGIN {FS = ":.*?## "} /^[a-zA-Z_-]+:.*?## / {printf "$(PRIMARY_TEXT_COLOR)%-30s$(NORMAL_TEXT_COLOR) %s\n", $$1, $$2}' $(MAKEFILE_LIST)


clean: ## Clean
	$(call pprint, "[${GROUP_ID}/${ARTIFACT_ID}] Clean")
	rm -rf .cljs_node_repl out ${TARGET} ${JAVA_TARGET} .shadow-cljs public/js public/test/js


repl: ## Run REPL
	$(call pprint, "[${GROUP_ID}/${ARTIFACT_ID}] Run REPL")
	npm run dev


lint: ## Run linter
	$(call pprint, "[${GROUP_ID}/${ARTIFACT_ID}] Run linter")
	clj-kondo --lint src/main/clojure:src/dev/clojure:src/test/clojure


test: clean lint ## Run tests
	$(call pprint, "[${GROUP_ID}/${ARTIFACT_ID}] Run tests")
	npm run test


build.edn: ## Generate build.edn
	$(call pprint, "[${GROUP_ID}/${ARTIFACT_ID}] Generate build.edn")
	@echo "EMAIL:          ${EMAIL}"
	@echo "LICENSE:        ${LICENSE}"
	@echo ""
	@echo "OS:             ${OS}"
	@echo "TIMESTAMP:      ${TIMESTAMP}"
	@echo ""
	@echo "GROUP_ID:       ${GROUP_ID}"
	@echo "ARTIFACT_ID:    ${ARTIFACT_ID}"
	@echo "VERSION:        ${VERSION}"
	@echo ""
	@echo "GIT_URL:        ${GIT_URL}"
	@echo "GIT_SHA:        ${GIT_SHA}"
	@echo ""
	@echo "REPOSITORY_URL: ${REPOSITORY_URL}"
	@echo ""
	@echo "RESOURCES:      ${RESOURCES}"
	@echo "TARGET:         ${TARGET}"
	@echo ""
	@echo "BUILD_FILE:     ${BUILD_FILE}"
	@echo "JAR_FILE:       ${JAR_FILE}"
	@mkdir -p ${RESOURCES}
	@echo -e \
	'{:email       "${EMAIL}"\r\n' \
	':license     "${LICENSE}"\r\n' \
	':group-id    "${GROUP_ID}"\r\n' \
	':artifact-id "${ARTIFACT_ID}"\r\n' \
	':version     "${VERSION}"\r\n' \
	':git/url     "${GIT_URL}"\r\n' \
	':git/sha     "${GIT_SHA}"\r\n' \
	':timestamp   "${TIMESTAMP}"}' > ${BUILD_FILE}


pom.xml: ## Generate pom.xml
	$(call pprint, "[${GROUP_ID}/${ARTIFACT_ID}] Generate pom.xml")
	clojure -Spom
	clojure -A:build --main garamond.main --pom --group-id ${GROUP_ID} --artifact-id ${ARTIFACT_ID} --force-version ${VERSION} --scm-url ${GIT_URL}


prepare: clean build.edn pom.xml ## Prepare to build


jar: prepare ## Build jar file
	$(call pprint, "[${GROUP_ID}/${ARTIFACT_ID}] Build jar file")
	clojure -A:build --main hf.depstar.jar ${JAR_FILE} --no-pom


js: prepare ## Build js files
	$(call pprint, "[${GROUP_ID}/${ARTIFACT_ID}] Build js files")
	npm run build


serve: ## Run local server
	$(call pprint, "[${GROUP_ID}/${ARTIFACT_ID}] Run local server")
	npm run serve


install: ## Install jar file
	$(call pprint, "[${GROUP_ID}/${ARTIFACT_ID}] Install jar file")
	mvn install:install-file -Dfile=${JAR_FILE} -DpomFile=pom.xml


deploy: ## Deploy to clojars
	$(call pprint, "[${GROUP_ID}/${ARTIFACT_ID}] Deploy to clojars")
	mvn -B deploy -DaltDeploymentRepository=clojars::default::${REPOSITORY_URL} -DskipTests
