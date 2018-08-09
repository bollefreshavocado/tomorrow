<template>
    <div id="main">
        <div class="list">
            <div class="list-header">
                <h2 class="list-title">Open tasks</h2>
            </div>
            <div class="card-list" v-for="(item, index) in tasks" :key='index'>

                <div class="card" v-bind:id=tasks[index].id v-on:mouseover="setHoveredTask" v-on:mouseout="unsetHoveredTask">

                    <span class="card-title" v-bind:id=tasks[index].id 
                       v-on:focus="saveUneditState"
                       v-on:click="makeEditable" 
                       v-on:keydown="submitChange" v-on:blur="submitChangeBlur">{{ tasks[index].content }}</span></div>
            
            </div>
            <a id="task-adder" v-on:click="addCard">Add a card...</a>
        </div>
        <div class="list">
            <div class="list-header">
                <h2 class="list-title">{{  getDayName(1) }}</h2>
            </div>
            <div class="card-list">
                <div class="card"><a class="card-title">Lorem ipsum dolor sit amet, consectetur adipiscing elit.</a></div>
            </div>
        </div>
        <div class="list">
            <div class="list-header">
                <h2 class="list-title">{{ getDayName(2) }}</h2>
            </div>
            <div class="card-list">
                <div class="card"><a class="card-title">LoremwA ipsum dolor sit amet, consectetur adipiscing elit.</a></div>
            </div>
        </div>
        <div class="list">
            <div class="list-header">
                <h2 class="list-title">{{ getDayName(3) }}</h2>
            </div>
            <div class="card-list">
                <div class="card"><a class="card-title">Lorem ipsum dolor sit amet, consectetur adipiscing elit.</a></div>
            </div>
        </div>
    </div>
</template>

<script>
import axios from "axios";
import moment from "moment";
import $ from "jquery";

//TODO: Das delete verhalten nach dem hinzufügen von karten stimmt nicht. 
//Ich denke task_save oder active oder unedit_task_save ist falsch gesetzt

export default {
  name: "Home",

  data() {
    return {
      title: null,
      tasks: null,
      username: null,
      editable: false,
      task_saved: false,
      unedit_task_state: null,
      addAnotherCard: false 
    };
  },

  methods: {
    getDayName: function(dayshifter) {
      return moment()
        .add(dayshifter, "days")
        .format("dddd");
    },

    addCard: function(event) {

      document.removeEventListener("keypress", this.deleteTask);

      var div_card = document.createElement("div");
      var a_title = document.createElement("span");

      a_title.className = "card-title";
      a_title.addEventListener("blur", this.submitChangeBlur, false);
      a_title.addEventListener("focus", this.saveUneditState, false);
      a_title.addEventListener("keydown", this.submitChange, false);
      a_title.setAttribute("contenteditable", true);
      a_title.setAttribute("id", "not_assigned_yet");

      div_card.className = "card";
      div_card.setAttribute("id", "not_assigned_yet");
      div_card.addEventListener("mouseover", this.setHoveredTask, false);
      div_card.addEventListener("mouseout", this.unsetHoveredTask, false);

      div_card.append(a_title);
      $(event.target).before(div_card);

      a_title.focus();
    },

    makeEditable : function (event) {

      //TODO: Wenn eine Karte editiert wird sollte ihr background wieder weiß sein
      document.removeEventListener("keypress", this.deleteTask);
      event.target.setAttribute("contentEditable", true);
      event.target.setAttribute("active", true);
      this.saveUneditState(event);
    },

    deleteTask: function(event) {

      //Check if currently mouse is over a task. If yes hover_task = 1. If no hover_task = 0
      var hover_task = ($("[active = true]").length);

      //If user presses the "d"-key and is hovering over a task, delete it.
      if (event.keyCode == 100 && hover_task == 1) {
        var task_id = $("[active = true]").prop("id");
        for (var i = 0; i < this.tasks.length; i++) {
          if (this.tasks[i].id == task_id) {
            console.log(this.tasks[i].content);
            console.log(i, "For-Loop i =");
            console.log(task_id, "Task_IDDD");
            this.tasks.splice(i, 1);
          }
        }
        axios
          .delete(
            "http://localhost:3029/api/user/" +
              this.$route.params.userId +
              "/" +
              task_id
          )
          .then(response => {
            // JSON responses are automatically parsed.
            console.log(response);
          })
          .catch(e => {
            this.errors.push(e);
          });
      }
    },

    saveUneditState: function(event) {
      
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

      this.task_saved = false;
      this.unedit_task_state = event.target.innerHTML;

    },

    setHoveredTask: function(event) {
      event.target.setAttribute("active", true);
    },

    unsetHoveredTask: function(event) {
      event.target.removeAttribute("active");
    },

    submitChangeBlur: function(event) {
      if (this.task_saved == true) {
        this.unedit_task_state = null;
        this.task_saved = false;
        return false;
      } else {
        if(event.target.innerHTML == "" && event.target.getAttribute("id") == "not_assigned_yet"){
          event.target.parentNode.outerHTML = "";
          document.addEventListener("keypress", this.deleteTask);
        } else if (event.target.innerHTML == "") {
          event.target.focus();
        } else {
          document.addEventListener("keypress", this.deleteTask);
          event.target.removeAttribute("contentEditable");
          event.keyCode = 13;
          this.submitChange(event);
        }
      }
    },

    submitChange: function(event) {
      if (event.keyCode == 13) {

        event.preventDefault();

        if (
          this.task_saved != true &&
          this.unedit_task_state != event.target.innerHTML
        ) {
          if (event.target.id != "not_assigned_yet") {
            console.log("Only Editing");
            axios
              .put(
                "http://localhost:3029/api/user/" +
                  this.$route.params.userId +
                  "/" +
                  event.target.id,
                {
                  content: event.target.innerHTML,
                  priority: "low"
                }
              )
              .then(response => {
                // JSON responses are automatically parsed.
                console.log(response);
                for (var i = 0; i < this.tasks.length; i++) {
                  if (this.tasks[i].id == event.target.id) {
                    this.tasks[i].content = event.target.innerHTML;
                  }
                }
                
                document.addEventListener("keypress", this.deleteTask);
              })
              .catch(e => {
                this.errors.push(e);
              });
          } else {
            console.log("NEW CARD!");
            axios
              .post(
                "http://localhost:3029/api/user/" +
                  this.$route.params.userId +
                  "/tasks",
                {
                  content: event.target.innerHTML,
                  priority: "low"
                }
              )
              .then(response => {
                // JSON responses are automatically parsed.
                if(event.defaultPrevented == true){
                  this.addAnotherCard = true;
                }
                event.target.parentNode.outerHTML = "";
                this.tasks.push(response.data);
                console.log(response);
              })
              .catch(e => {
                this.errors.push(e);
              });
          }
          this.task_saved = true;
          $(":focus").blur();
        }
      }
    }
  },

  // Fetches tasks when the component is created.
  created() {
    document.addEventListener("keypress", this.deleteTask);
    //axios.defaults.headers.common['Content-Type'] = 'application/json'
    axios
      .get(
        "http://localhost:3029/api/user/" + this.$route.params.userId + "/tasks"
      )
      .then(response => {
        // JSON responses are automatically parsed.
        this.tasks = response.data;
      })
      .catch(e => {
        this.errors.push(e);
      });
  },

  updated: function (){
    if(this.addAnotherCard){
      this.addAnotherCard = false;
      document.getElementById('task-adder').click();
    }
  }
};
</script>

<style>
#main {
  text-align: center;
  font-family: "Helvetica Neue", Arial, Helvetica, sans-serif;
  padding: 10px;
}
.list {
  margin: 5px;
  border: 1px solid rgb(204, 204, 204);
  border-radius: 3px;
  display: inline-grid;
  max-height: 450px;
  width: 270px;
  padding: 4px 4px 0px;
  background-color: rgb(226, 228, 230);
}
.list-header {
  padding: 8px 4px;
  position: relative;
  min-height: 19px;
  display: block;
  line-height: 18px;
  text-align: left;
}
.list-title {
  cursor: pointer;
  display: inline;
  font-size: 15px;
  font-weight: bold;
  line-height: 18px;
  margin: 0px;
  min-height: 19px;
  min-width: 30px;
  overflow: hidden;
  text-overflow: ellipsis;
  word-wrap: break-word;
  color: rgb(77, 77, 77);
}
.card-list {
  overflow-y: auto;
  overflow-x: hidden;
  max-height: 410px;
}
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
#task-adder {
  display: block;
  color: rgb(128, 128, 128);
  font-size: 14px;
  margin: 0 -4px;
  line-height: 30px;
  width: 278px;
  text-align: center;
}
#task-adder:hover {
  background-color: rgb(209, 209, 209);
  text-decoration: underline;
  cursor: pointer;
}
</style>
