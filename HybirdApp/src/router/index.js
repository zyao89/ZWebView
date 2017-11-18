import HelloWorld from '@/components/HelloWorld'
import ECharts from '@/components/ECharts'
import GISMap from '@/components/GISMap'
import ColorManipulation from '@/components/ColorManipulation'

export default new VueRouter({
  routes: [
    {
      path: '/',
      name: 'HelloWorld',
      component: HelloWorld
    },
    {
      path: '/echarts',
      name: 'ECharts',
      component: ECharts
    },
    {
      path: '/gis',
      name: 'GISMap',
      component: GISMap
    },
    {
      path: '/manipulation',
      name: 'ColorManipulation',
      component: ColorManipulation
    }
  ]
})
