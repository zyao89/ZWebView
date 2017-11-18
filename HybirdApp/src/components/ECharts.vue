<template>
  <!-- 为 ECharts 准备一个具备大小（宽高）的 DOM -->
  <div>
    <div id="Chart1" style="height:400px;"></div>
    <div id="Chart2" style="height:400px;"></div>
  </div>
</template>

<script>
// import Echarts from "echarts";
export default {
  name: "ECharts",
  zWebReady() {
    console.log("ECharts ready.call.......");
    // 基于准备好的dom，初始化echarts实例
    let myChart1 = echarts.init(document.getElementById("Chart1"));
    let myChart2 = echarts.init(document.getElementById("Chart2"));

    ZWebSDK.message("Chart1", "我要加载数据。。。").then(
      function(data) {
        console.info(data);
        // 绘制图表
        myChart1.setOption(JSON.parse(data));
      },
      function(data) {
        ZWebSDK.tip(data);
      }
    );
    ZWebSDK.message("Chart2", "我要加载数据。。。").then(
      function(data) {
        console.info(data);
        // 绘制图表
        myChart2.setOption(JSON.parse(data));
      },
      function(data) {
        ZWebSDK.tip(data);
      }
    );
  }
};
</script>

<style>

</style>
