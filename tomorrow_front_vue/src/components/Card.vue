<template>
    <div class="card" @click="editContent" @mouseenter="setHoveredTask" @mouseleave="unsetHoveredTask" :id= this.id>
        <div class="card-title unselectable" @focus="saveStateAndHighlight" @blur="leaveEdit" @keypress.enter="leaveCard">
            {{ content }}
        </div>
    </div>
</template>

<script>

import { EventBus } from '../eventBus';
import $ from 'jquery';

export default {
  name: "Card",

  props: {
    content: String,
    id: Number,
  },

  methods: {

    leaveCard(evt){
      evt.target.blur();
    },

    saveStateAndHighlight(event) {
      window.setTimeout(function() {
        var sel, range;
        if (window.getSelection && document.createRange) {
          range = document.createRange();
          range.selectNodeContents(event.target);
          sel = window.getSelection();
          sel.removeAllRanges();
          sel.addRange(range);
        } else if (document.body.createTextRange) {
          range = document.body.createTextRange();
          range.moveToElementText(event.target);
          range.select();
        }
      }, 1);

      //HIER FEHLERN NOCH 2 ZEILEN VOM ORGINAL!!!
    },

    leaveEdit(event){
      EventBus.$emit('addDeleteListener');
      //HIER GEHTS WEITER WEGEN DEM CONTENT
      EventBus.$emit('adaptContent', this.id, event.target.innerHTML);
      event.target.removeAttribute("contentEditable");
    },

    setHoveredTask(event) {
      event.target.setAttribute("active", true);
    },

    unsetHoveredTask(event) {
      event.target.removeAttribute("active");
    },

    editContent(event){
      EventBus.$emit('removeDeleteListener');
      if( event.target.children.length == 1){
        event.target.children[0].setAttribute("contenteditable", true)
        event.target.children[0].focus();
      } else {
        event.target.setAttribute("contenteditable", true);
        event.target.focus();
      }
    },

  },

  data(){
    return {
    }
  }
}
</script>

<style>
.card {
  overflow: auto;
  padding: 6px 8px 4px;
  margin-bottom: 4px;
  position: relative;
  display: block;
  cursor: pointer;
  color: rgb(77, 77, 77);
  font-size: 14px;
  line-height: 18px;
  border: 1px solid rgb(204, 204, 204);
  border-radius: 4px;
  width: 250px;
  text-align: left;
  background-color: rgb(255, 255, 255);
}
.card:hover {
  background-color: rgb(236, 236, 236);
}
.card-title {
  pointer-events: none;
  color: rgb(77, 77, 77);
  clear: both;
  display: block;
  font-weight: 400;
  margin: 0px 0px 4px;
  overflow: hidden;
  text-decoration: none;
  word-wrap: break-word;
  background-color: transparent;
}
#delete-button{
  display: block;
  overflow: hidden;
  text-decoration: none;
  background-color: transparent;
}
.unselectable {
    -webkit-touch-callout: none;
    -webkit-user-select: none;
    -khtml-user-select: none;
    -moz-user-select: none;
    -ms-user-select: none;
    -o-user-select: none;
    user-select: none;
}
</style>
