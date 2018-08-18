<template>
    <div id="main" :my-prop="alltasks">
        <app-cardlist title="To-Do" :cards="todos"></app-cardlist>
        <app-cardlist :title= futureDays[0].day :cards="tomorrow"></app-cardlist>
        <app-cardlist :title= futureDays[1].day :cards="in2days"></app-cardlist>
        <app-cardlist :title= futureDays[2].day :cards="in3days"></app-cardlist>
    </div>
</template>

<script>
import axios from 'axios';
import $ from "jquery";
import moment from "moment";
import CardList from './CardList';
import { EventBus } from '../eventBus';

export default {
  name: "Lists",

  props: {
    alltasks: Array,
  },

  components: {
    'app-cardlist' : CardList
  },

  watch: {
    alltasks: function(){
      this.listFiller();
    }
  },

  created(){
    this.listFiller();
    EventBus.$on('addCard', (cardDate) => { this.addCard(cardDate); })
    EventBus.$on('deleteCard', () => { this.deleteCard(); })
    EventBus.$on('adaptContent', (id, newContent) => { this.adaptContent(id, newContent); })
  },

  methods:{

    deleteCard(){

        var deletedCardID = $("[active = true]").prop("id");

        var currentList = $("[active = true]").first().parent().parent().children().first().prop("innerText");

        if(currentList == "To-Do"){
          for (var i = 0; i < this.todos.length; i++) {
            if(this.todos[i].id == deletedCardID){
              this.todos.splice(i, 1);
            }
          }
        } else if (currentList == this.getDayName(1)){
          for (var i = 0; i < this.tomorrow.length; i++) {
            if(this.tomorrow[i].id == deletedCardID){
              this.tomorrow.splice(i, 1);
            }
          }
        } else if(currentList == this.getDayName(2)){
          for (var i = 0; i < this.in2days.length; i++) {
            if(this.in2days[i].id == deletedCardID){
              this.in2days.splice(i, 1);
            }
          }
        } else if(currentList == this.getDayName(3)){
          for (var i = 0; i < this.in3days.length; i++) {
            if(this.in3days[i].id == deletedCardID){
              this.in3days.splice(i, 1);
            }
          }
        }

        axios
          .delete(
            "http://localhost:3029/api/user/" +
              this.$route.params.userId +
              "/" +
              deletedCardID
          )
          .then(response => {
            // JSON responses are automatically parsed.
            console.log(response);
          })
          .catch(e => {
            this.errors.push(e);
          });
    },

    adaptContent(id, newContent){
      console.log("HOW OFTEN");
      var cardDate;
      var currentList = $("[active = true]").first().parent().parent().children().first().prop("innerText");

      if(currentList == "To-Do"){
          for (var i = 0; i < this.todos.length; i++) {
            if(this.todos[i].id == id){
              this.todos[i].content = newContent;
              cardDate = this.todos[i].date;
            }
          }
        } else if (currentList == this.getDayName(1)){
          for (var i = 0; i < this.tomorrow.length; i++) {
            if(this.tomorrow[i].id == id){
              this.tomorrow[i].content = newContent;
              cardDate = this.tomorrow[i].date;
            }
          }
        } else if(currentList == this.getDayName(2)){
          for (var i = 0; i < this.in2days.length; i++) {
            if(this.in2days[i].id == id){
              this.in2days[i].content = newContent;
              cardDate = this.in2days[i].date;
            }
          }
        } else if(currentList == this.getDayName(3)){
          for (var i = 0; i < this.in3days.length; i++) {
            if(this.in3days[i].id == id){
              this.in3days[i].content = newContent;
              cardDate = this.in3days[i].date;
            }
          }
        }

        console.log(cardDate);
        console.log(this.tomorrow);

      axios
        .put(
          "http://localhost:3029/api/user/" +
            this.$route.params.userId +
            "/" +
            id,
          {
            content: newContent,
            date: cardDate
          }
        )
        .then(response => {
          // JSON responses are automatically parsed.
          console.log(response);
        })
        .catch(e => {
          this.errors.push(e);
        });
    },

    //Used to retrieve day names. Example: If today is tuesday and you set dayshifter 1 you get wednesday
    getDayName(dayshifter) {
      return moment()
        .add(dayshifter, "days")
        .format("dddd")
        .toString();
    },

    //Fills the lists with its Cards
    listFiller(){
      for (var i = 0; i < this.alltasks.length; i++) {
        if(this.alltasks[i].date == null){
            this.todos.push(this.alltasks[i]);
        } else if (this.alltasks[i].date == moment().add(1, 'd').format("DD.MM.YYYY")){
            this.tomorrow.push(this.alltasks[i]);
        } else if (this.alltasks[i].date == moment().add(2, 'd').format("DD.MM.YYYY")){
            this.in2days.push(this.alltasks[i]);
        } else if (this.alltasks[i].date == moment().add(3, 'd').format("DD.MM.YYYY")){
            this.in3days.push(this.alltasks[i]);
        }
      }
    }

  },


  data(){
    return {
      futureDays: [ 
        {day: this.getDayName(1)}, 
        {day: this.getDayName(2)}, 
        {day: this.getDayName(3)} 
      ],

      todos: [],
      tomorrow: [],
      in2days: [],
      in3days: []
    }
  }
}
</script>

<style>
#main {
  text-align: center;
  font-family: "Helvetica Neue", Arial, Helvetica, sans-serif;
  padding: 20px;
}
</style>
