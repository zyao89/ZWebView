<template>
  <div>
    <a class="skiplink" href="#map">Go to map</a>
    <div id="map" class="map" tabindex="0"></div>
    <button id="zoom-out">Zoom out</button>
    <button id="zoom-in">Zoom in</button>
  </div>
</template>

<script>
export default {
  name: "GISMap",
  mounted() {
    var map = new ol.Map({
      layers: [
        new ol.layer.Tile({
          source: new ol.source.OSM()
        })
      ],
      target: "map",
      controls: ol.control.defaults({
        attributionOptions: ({
          collapsible: false
        })
      }),
      view: new ol.View({
        center: [0, 0],
        zoom: 2
      })
    });

    map.addControl(new ol.control.FullScreen());

    document.getElementById("zoom-out").onclick = function() {
      var view = map.getView();
      var zoom = view.getZoom();
      view.setZoom(zoom - 1);
    };

    document.getElementById("zoom-in").onclick = function() {
      var view = map.getView();
      var zoom = view.getZoom();
      view.setZoom(zoom + 1);
    };
  }
};
</script>

<style>
a.skiplink {
  position: absolute;
  clip: rect(1px, 1px, 1px, 1px);
  padding: 0;
  border: 0;
  height: 1px;
  width: 1px;
  overflow: hidden;
}
a.skiplink:focus {
  clip: auto;
  height: auto;
  width: auto;
  background-color: #fff;
  padding: 0.3em;
}
#map:focus {
  outline: #4a74a8 solid 0.15em;
}
#map {
  width: 100%;
  height: 100%;
}
</style>
