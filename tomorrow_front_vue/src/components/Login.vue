<template>
<!DOCTYPE html>
    <html lang="en" >
        <head>
        <meta charset="UTF-8">
        <title>Material design sign up form</title>
            <link rel="stylesheet" href="/assets/style.css">
        </head>
        <body>
        <div id="login-box">
            <div class="left">
                <h1>Sign up</h1>
                
                <input type="text" v-model="username" placeholder="Username" />
                <input type="text" v-model="emailadress" placeholder="E-mail" />
                <input type="password" v-model="password" placeholder="Password" />
                <input type="password" placeholder="Retype password" />
                
                <button type="submit" v-on:click="submitRegister">Submit</button>
            </div>
            <div class="right">
                <span class="loginwith">Sign in with<br />social network</span>
                
                <button class="social-signin facebook">Log in with facebook</button>
                <button class="social-signin twitter">Log in with Twitter</button>
                <button class="social-signin google">Log in with Google+</button>
            </div>
            <div class="or">OR</div>
            </div>
        </body>
    </html>
</template>

<script>
import axios from 'axios';

export default {
    name: 'Login',

    data() {
        return{
            emailadress: null,
            password: null,
            username: null,
            userId: null
        }
    },

    methods: {
        submitRegister() {

            let self = this;

            axios.post(`http://localhost:3029/api/user`, {
                userName: this.username,
                emailadress: this.emailadress,
                password: this.password
            })
            .then(function(response){
                if(response.data != "Email already in use by another account"){
                    self.$router.push('/home/' + response.data.id);
                } else {
                    console.log(response.data);
                }
            });
    }
  }
}
</script>



