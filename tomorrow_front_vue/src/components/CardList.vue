<template>
  <div class="list">
    <div class="list-header">
      <h2 class="list-title unselectable"> {{title}} </h2>
    </div>
    <draggable :list="cards" @add="adaptCardDate" class="card-list" :options="{group: 'cards'}">
        <app-card  v-for="(item, index) in cards" :key='index' :content=cards[index].content :id=cards[index].id></app-card>
    </draggable>
    <a class="unselectable" id="task-adder" v-on:click="addCard">Add a card...</a>
  </div>
</template>

<script>
import axios from 'axios';
import draggable from 'vuedraggable';
import Card from './Card';
import $ from "jquery";
import moment from "moment";
import { EventBus } from '../eventBus';

export default {
  name: "CardList",

  props: {
    title: String,
    cards: Array,
  },

  components: {
    'app-card': Card,
    'draggable' : draggable,
  },

  data(){
    return {

    }
  },

  methods: {

    //TODO: Add ID und Text soll direkt gehighlighted sein
    //TODO: Wenn die Liste voll ist sollte sie automatisch nach unten scrollen wenn man neue Karten hinzufügt
    addCard(){
      axios
        .post(
          "http://localhost:3029/api/user/" +
            this.$route.params.userId +
            "/tasks",
          {
            content: "Tell me a task",
            priority: "low",
            date: this.listChecker()
          }
        )
        .then(response => {
          // JSON responses are automatically parsed.;
          this.cards.push(response.data);
          console.log(response);
        })
        .catch(e => {
          this.errors.push(e);
        });
    },

    adaptCardDate(evt){
      //Removed active because else we have wrong delete behaviour
      //evt.explicitOriginalTarget.removeAttribute("active");

      var movedCardIndex;

      for (var i = 0; i < this.cards.length; i++) {
        if(this.cards[i].date != this.listChecker()){
          movedCardIndex = i;
          this.cards[i].date = this.listChecker();
        }
      }

      axios
        .put(
          "http://localhost:3029/api/user/" +
            this.$route.params.userId +
            "/" +
            this.cards[movedCardIndex].id,
          {
            date: this.cards[movedCardIndex].date,
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

    listChecker(){
      var cardDate
      if(this.title == "To-Do"){
          cardDate = null;
      } else {
        cardDate = moment().day(this.title)
        if(moment().isAfter(cardDate)){
          cardDate.add(1, "weeks");
        }
        cardDate = cardDate.format("DD.MM.YYYY");
      }
      return cardDate;
    },

  },
}

</script>

<style>
.list {
  margin: 5px;
  border: 1px solid rgb(204, 204, 204);
  border-radius: 3px;
  display: inline-grid;
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
  cursor: default;
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
  min-height: 20px;
  overflow-y: auto;
  overflow-x: hidden;
  max-height: 87vh;
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
