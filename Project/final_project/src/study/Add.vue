<template>
    <div id="ss">
        <h2>{{msg}}</h2>
        <button @click="add">+</button>
        <button v-on:click="sub">-</button>
        <h2>{{count*2}}</h2>
    </div>
</template>

<script>
    export default {
        name: "Add",
        data(){
            return{
                msg:1,
                count:2
            }
        },
        methods:{
            add(){
                this.msg++
            },
            sub(){
                this.msg--
            }
        },

    }
</script>

<style scoped>

</style>