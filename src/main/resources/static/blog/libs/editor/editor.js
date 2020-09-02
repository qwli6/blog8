"use strict";
// let CodeMirror = require('codemirror');


// require("../codemirror-5.52.0/addon/edit/continuelist");
// require("../codemirror-5.52.0/addon/display/fullscreen");
// require('../codemirror-5.52.0/mode/markdown/markdown');
// require("../codemirror-5.52.0/addon/mode/overlay.js");
// require("../codemirror-5.52.0/addon/display/placeholder.js");
// require("../codemirror-5.52.0/addon/selection/mark-selection.js");
// require("../codemirror-5.52.0/mode/gfm/gfm.js");
// require("../codemirror-5.52.0/mode/xml/xml.js");




function BlogEditor(options) {

    options = options || {};

    options.parent = this;

    if(options.elem){
        this.elem = options.elem;
    } else if(options.elem === null){
        console.log("BlogEditor: Error. No element was found");
        return;
    }


    this.render();

}


BlogEditor.prototype.render = function(el){
    if(!el){
        el = this.elem || document.getElementsByTagName('textarea')[0];
    }


    this.elem = el;

    let options = this.options;

    let self = this;


    this.codemirror = CodeMirror.fromTextArea(el, {
        mode: "markdown",
        tableSize: 2,
        indentUnit: 2,
        lineNumbers: true,
    })
};

// module.exports = BlogEditor;