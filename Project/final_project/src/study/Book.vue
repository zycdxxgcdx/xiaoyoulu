<template>
  <div>
    <table>
      <tr>
        <td>编号</td>
        <td>图书名称</td>
        <td>作者</td>
      </tr>
      <tr v-for="item in books">
        <td>{{item.id}}</td>
        <td>{{item.name}}</td>
        <td>{{item.author}}</td>
      </tr>
    </table>
  </div>
</template>

<script>
  export default {
    name: "Book",
    data(){
      return {
        msg:'Hello Vue',
        books:[
          {
            id:1,
            name:'Java学习',
            author:'Tom',
          },
          {
            id:2,
            name:'Spring boot学习',
            author:'TIM',
          },
          {
            id:3,
            name:'Vue学习',
            author:'ROCK',
          }
        ]
      }
    },
    created(){
      const _this = this;
      axios.get('http://localhost:8181/book/findAll').then(function (resp) {
        _this.books = resp.data;
      })
    }

  }
</script>

<style scoped>

</style>