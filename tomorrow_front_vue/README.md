# tomorrow_front_vue

> This is a vue.js front-end which stacks on top of the tomorrow-api

## Build Setup

``` bash
# install dependencies
npm install

# serve with hot reload at localhost:8080
npm run dev

# build for production with minification
npm run build
```

For detailed explanation on how things work, consult the [docs for vue-loader](http://vuejs.github.io/vue-loader).

## Rough overview of components relations

![Image of Component-Stack](https://i.imgur.com/zAfV4iM.png)

## Bus communication

![Image of Bus communication](https://i.imgur.com/HuuPjQL.png)

## Little How-To

- If you want to create a card just click on the "Add a Card..."-button.
- If you want to delete a card hover over it and click the "d"-key on your keyboard.
- If you want to change the date when you want to do your task, just drag it to the desired list

## Next Steps

- Create one login-screen or button
- Bind the google login to the authentication service
- Remove static binds to the database
- The lists shouldn't get sorted (maybe add position index)
- Fix wrong card edit behaviour when blur is triggered by clicking outside of the card
