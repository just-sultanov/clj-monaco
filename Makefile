.PHONY: help test
.DEFAULT_GOAL := help

SHELL = bash

SCM_URL="https://github.com/just-sultanov/clj-monaco"
GROUP_ID=clj-monaco
ARTIFACT_ID=clj-monaco
TAG_MSG="release a new version"


help: ## Show help
	@awk 'BEGIN {FS = ":.*?## "} /^[a-zA-Z_-]+:.*?## / {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}' $(MAKEFILE_LIST)


dev: ## Run app in dev mode
	npm run dev


clean: ## Clean
	@echo "=================================================================="
	@echo "Clean..."
	@echo "=================================================================="
	rm -rf pom.xml clj-monaco.jar target out .cljs_node_repl .shadow-cljs public/js public/test/js
	@echo -e "\n"


lint: ## Run linter
	@echo "=================================================================="
	@echo "Run linter..."
	@echo "=================================================================="
	clj-kondo --lint src
	@echo -e "\n"


test: ## Run tests
	@echo "=================================================================="
	@echo "Run tests..."
	@echo "=================================================================="
	npm run test
	@echo -e "\n"


serve: ## Run local server
	@echo "=================================================================="
	@echo "Run local server..."
	@echo "=================================================================="
	npm run serve
	@echo -e "\n"


build: ## Build app
	@echo "=================================================================="
	@echo "Build app..."
	@echo "=================================================================="
	npm run build
	@echo -e "\n"


jar: ## Build jar
	@echo "=================================================================="
	@echo "Build jar..."
	@echo "=================================================================="
	clojure -A:build
	clojure -A:version --pom --group-id ${GROUP_ID} --artifact-id ${ARTIFACT_ID} --scm-url ${SCM_URL}
	@echo -e "\n"


init: ## Init first version
	git tag --annotate --message ${TAG_MSG} v0.0.1


patch: ## Increment patch version
	clojure -A:version patch --tag --message ${TAG_MSG}


minor: ## Increment minor version
	clojure -A:version minor --tag --message ${TAG_MSG}


major: ## Increment major version
	clojure -A:version major --tag --message ${TAG_MSG}


install: ## Install locally
	@echo "=================================================================="
	@echo "Install locally..."
	@echo "=================================================================="
	clojure -A:install
	@echo -e "\n"


release: ## Release a new version
	@echo "=================================================================="
	@echo "Release a new version..."
	@echo "=================================================================="
	git push origin --tags


deploy: ## Deploy to clojars
	@echo "=================================================================="
	@echo "Deploy..."
	@echo "=================================================================="
	clojure -A:deploy
	@echo -e "\n"