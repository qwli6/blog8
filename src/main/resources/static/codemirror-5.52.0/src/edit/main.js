// EDITOR CONSTRUCTOR

import {CodeMirror} from "./CodeMirror.js"
import {eventMixin} from "../util/event.js"
import {indexOf} from "../util/misc.js"

import {defineOptions} from "./options.js"
import addEditorMethods from "./methods.js"
import Doc from "../model/Doc.js"
import ContentEditableInput from "../input/ContentEditableInput.js"
import TextareaInput from "../input/TextareaInput.js"
import {defineMIME, defineMode} from "../modes.js"
import {fromTextArea} from "./fromTextArea.js"
import {addLegacyProps} from "./legacy.js"

export { CodeMirror } from "./CodeMirror.js"

defineOptions(CodeMirror)

addEditorMethods(CodeMirror)

// Set up methods on CodeMirror's prototype to redirect to the editor's document.
let dontDelegate = "iter insert remove copy getEditor constructor".split(" ")
for (let prop in Doc.prototype) if (Doc.prototype.hasOwnProperty(prop) && indexOf(dontDelegate, prop) < 0)
  CodeMirror.prototype[prop] = (function(method) {
    return function() {return method.apply(this.doc, arguments)}
  })(Doc.prototype[prop])

eventMixin(Doc)

// INPUT HANDLING
CodeMirror.inputStyles = {"textarea": TextareaInput, "contenteditable": ContentEditableInput}

// MODE DEFINITION AND QUERYING

// Extra arguments are stored as the mode's dependencies, which is
// used by (legacy) mechanisms like loadmode.js to automatically
// load a mode. (Preferred mechanism is the require/define calls.)
CodeMirror.defineMode = function(name/*, mode, …*/) {
  if (!CodeMirror.defaults.mode && name != "null") CodeMirror.defaults.mode = name
  defineMode.apply(this, arguments)
}

CodeMirror.defineMIME = defineMIME

// Minimal default mode.
CodeMirror.defineMode("null", () => ({token: stream => stream.skipToEnd()}))
CodeMirror.defineMIME("text/plain", "null")

// EXTENSIONS

CodeMirror.defineExtension = (name, func) => {
  CodeMirror.prototype[name] = func
}
CodeMirror.defineDocExtension = (name, func) => {
  Doc.prototype[name] = func
}

CodeMirror.fromTextArea = fromTextArea

addLegacyProps(CodeMirror)

CodeMirror.version = "5.52.0"
