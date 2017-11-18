<template>
  <div class="hello">
    <h1>{{ msg }}</h1>
    <h2>ECharts</h2>
    <ul>
      <li><router-link :to="{name: 'ECharts'}">Go to a simple Chart.</router-link></li>
    </ul>
    <h2>GIS Map</h2>
    <ul>
      <li><router-link :to="{name: 'GISMap'}">Simple One.</router-link></li>
      <li><router-link :to="{name: 'ColorManipulation'}">Simple Two</router-link></li>
    </ul>
  </div>
</template>

<script>
export default {
  name: "HelloWorld",
  data() {
    return {
      msg: "Your Hybird App"
    };
  },
  mounted() {},
  zWebReady(){
    console.log('HelloWorld ready.call.......')
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h1,
h2 {
  font-weight: normal;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #42b983;
}
.hello {
  margin-top: 60px;
}
</style>
