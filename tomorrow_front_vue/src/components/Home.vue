<template>
  <div>
    <app-header></app-header>
    <app-lists :alltasks = this.alltasks></app-lists>
  </div>
</template>

<script>
import axios from "axios";
import $ from "jquery";
import moment from "moment";
import Lists from './Lists';
import Header from './Header';
import { EventBus } from '../eventBus';

export default {
  name: "Home",

  components: {
    'app-lists' : Lists,
    'app-header' : Header
  },

  methods:{

    deleteTask(event){
        if(event.which == 100 && ($("[active = true]").length) != 0){
            EventBus.$emit('deleteCard');
        }
    }

  },

  created() {
    document.addEventListener("keypress", this.deleteTask);
    EventBus.$on('removeDeleteListener', () => { document.removeEventListener("keypress", this.deleteTask); })
    EventBus.$on('addDeleteListener', () => { document.addEventListener("keypress", this.deleteTask); })
    
    axios
    .get(
      "http://localhost:3029/api/user/" + this.$route.params.userId + "/tasks"
    )
    .then(response => {
      // JSON responses are automatically parsed.
      this.alltasks = response.data;
      console.log(this.alltasks);
    })
    .catch(e => {
      this.errors.push(e);
    });
  },


  data(){
    return {

      alltasks: [
      ],

    }
  }
}
</script>

<style>
</style>
