;;;;
;; Copyright © 2019-2020 anywhere.ninja. All rights reserved.
;; The use and distribution terms for this software are covered by the license
;; which can be found in the file license at the root of this distribution.
;; By using this software in any fashion, you are agreeing to be bound by the
;; terms of this license.
;; You must not remove this notice, or any other, from this software.
;;;;

(ns monaco.api.keyboard
  "A Monaco keyboard API."
  (:require
    [monaco.js-interop :as j]
    [monaco.api.refs :refer [Monaco]]))

(def ^{:added "0.0.8"}
  codes
  "Normalized key codes."
  {:abnt-c1                 :ABNT_C1
   :abnt-c2                 :ABNT_C2
   :alt                     :Alt
   :backspace               :Backspace
   :caps-lock               :CapsLock
   :context-menu            :ContextMenu
   :ctrl                    :Ctrl
   :delete                  :Delete
   :down-arrow              :DownArrow
   :end                     :End
   :enter                   :Enter
   :escape                  :Escape
   :f1                      :F1
   :f10                     :F10
   :f11                     :F11
   :f12                     :F12
   :f13                     :F13
   :f14                     :F14
   :f15                     :F15
   :f16                     :F16
   :f17                     :F17
   :f18                     :F18
   :f19                     :F19
   :f2                      :F2
   :f3                      :F3
   :f4                      :F4
   :f5                      :F5
   :f6                      :F6
   :f7                      :F7
   :f8                      :F8
   :f9                      :F9
   :home                    :Home
   :insert                  :Insert
   :key-0                   :KEY_0
   :key-1                   :KEY_1
   :key-2                   :KEY_2
   :key-3                   :KEY_3
   :key-4                   :KEY_4
   :key-5                   :KEY_5
   :key-6                   :KEY_6
   :key-7                   :KEY_7
   :key-8                   :KEY_8
   :key-9                   :KEY_9
   :key-a                   :KEY_A
   :key-b                   :KEY_B
   :key-c                   :KEY_C
   :key-d                   :KEY_D
   :key-e                   :KEY_E
   :key-f                   :KEY_F
   :key-g                   :KEY_G
   :key-h                   :KEY_H
   :key-i                   :KEY_I
   :key-in-composition      :KEY_IN_COMPOSITION
   :key-j                   :KEY_J
   :key-k                   :KEY_K
   :key-l                   :KEY_L
   :key-m                   :KEY_M
   :key-n                   :KEY_N
   :key-o                   :KEY_O
   :key-p                   :KEY_P
   :key-q                   :KEY_Q
   :key-r                   :KEY_R
   :key-s                   :KEY_S
   :key-t                   :KEY_T
   :key-u                   :KEY_U
   :key-v                   :KEY_V
   :key-w                   :KEY_W
   :key-x                   :KEY_X
   :key-y                   :KEY_Y
   :key-z                   :KEY_Z
   :left-arrow              :LeftArrow
   :max-value               :MAX_VALUE
   :meta                    :Meta
   :numpad-0                :NUMPAD_0
   :numpad-1                :NUMPAD_1
   :numpad-2                :NUMPAD_2
   :numpad-3                :NUMPAD_3
   :numpad-4                :NUMPAD_4
   :numpad-5                :NUMPAD_5
   :numpad-6                :NUMPAD_6
   :numpad-7                :NUMPAD_7
   :numpad-8                :NUMPAD_8
   :numpad-9                :NUMPAD_9
   :numpad-add              :NUMPAD_ADD
   :numpad-decimal          :NUMPAD_DECIMAL
   :numpad-divide           :NUMPAD_DIVIDE
   :numpad-multiply         :NUMPAD_MULTIPLY
   :numpad-separator        :NUMPAD_SEPARATOR
   :numpad-subtract         :NUMPAD_SUBTRACT
   :num-lock                :NumLock
   :oem-102                 :OEM_102
   :oem-8                   :OEM_8
   :page-down               :PageDown
   :page-up                 :PageUp
   :pause-break             :PauseBreak
   :right-arrow             :RightArrow
   :scroll-lock             :ScrollLock
   :shift                   :Shift
   :space                   :Space
   :tab                     :Tab
   :us-backslash            :US_BACKSLASH
   :us-backtick             :US_BACKTICK
   :us-close-square-bracket :US_CLOSE_SQUARE_BRACKET
   :us-comma                :US_COMMA
   :us-dot                  :US_DOT
   :us-equal                :US_EQUAL
   :us-minus                :US_MINUS
   :us-open-square-bracket  :US_OPEN_SQUARE_BRACKET
   :us-quote                :US_QUOTE
   :us-semicolon            :US_SEMICOLON
   :us-slash                :US_SLASH
   :unknown                 :Unknown
   :up-arrow                :UpArrow})


(def ^{:added "0.0.8"}
  modifiers
  "Normalized key modifiers."
  {:alt      :Alt
   :ctrl-cmd :CtrlCmd
   :shift    :Shift
   :win-ctrl :WinCtrl})



(defn with-key
  "Returns a key code by the given key - `monaco.KeyCode.{ key }`

  Keys:
    * `:abnt-c1`
    * `:abnt-c2`
    * `:alt`
    * `:backspace`
    * `:caps-lock`
    * `...`
    * `:up-arrow`

  Returns:
    * `number` or `nil`

  Full list of available keys:
    * [link](https://microsoft.github.io/monaco-editor/api/enums/monaco.keycode.html)"
  {:added "0.0.8"}
  [key]
  (let [k (get codes key key)]
    (j/get-in* Monaco [:key-code k])))


(defn with-modifier
  "Returns a key modifier code by the given key - `monaco.KeyMod.{ key }`

  Keys:
    * `:alt`
    * `:ctrl-cmd`
    * `:shift`
    * `:win-ctrl`

  Full list of available modifiers:
    * [link](https://microsoft.github.io/monaco-editor/api/classes/monaco.keymod.html)"
  {:added "0.0.8"}
  [key]
  (let [k (get modifiers key key)]
    (j/get-in* Monaco [:key-mod k])))


(defn with-combo
  "Creates a combination of keys."
  {:added "0.0.8"}
  [modifier & keys]
  (let [m  (with-modifier modifier)
        ks (map with-key keys)]
    (apply bit-or m ks)))


(def ^{:added "0.0.8"}
  with-chord
  "Creates a chord.

  Params:
    * `first-part`  - `number`
    * `second-part` - `number`

  Returns:
   * `number`"
  (let [f (j/get-in Monaco [:KeyMod :chord])]
    (fn [first-part second-part]
      (f first-part second-part))))





(comment
  ;; Simple key: `tab`
  (with-key :tab)
  ;; => 2

  ;; Simple modifier: `cmd`
  (with-modifier :ctrl-cmd)
  ;; => 2048

  ;; Simple combination: `cmd + k`
  (with-combo :ctrl-cmd :key-k)
  ;; => 2089

  ;; Complex combination: `cmd + k cmd + m`
  (with-chord
    (with-combo :ctrl-cmd :key-k)
    (with-combo :ctrl-cmd :key-m))
  ;; => 137037865
  )
