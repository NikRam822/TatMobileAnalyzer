<template>
  <Bar :data="param" :options="options" :chart-id="chartId" :dataset-id-key="datasetIdKey" :plugins="plugins"
    :css-classes="cssClasses" :styles="styles" :width="width" :height="height" />
</template>

<script>
import { Bar } from 'vue-chartjs';
import { mapState } from 'vuex';
import {
  Chart,
  Title,
  Tooltip,
  Legend,
  BarElement,
  CategoryScale,
  LinearScale,
} from 'chart.js';

Chart.register(
  Title,
  Tooltip,
  Legend,
  BarElement,
  CategoryScale,
  LinearScale
);
Chart.defaults.color = "#FFF"
export default {
  name: 'BarChart',
  components: {
    Bar,
  },
  props: {
    chartId: {
      type: String,
      default: 'bar-chart',
    },
    datasetIdKey: {
      type: String,
      default: 'label',
    },
    width: {
      type: Number,
      default: 400,
    },
    height: {
      type: Number,
      default: 400,
    },
    cssClasses: {
      default: '',
      type: String,
    },
    styles: {
      type: Object,
      default: () => ({}),
    },
    plugins: {
      type: Array,
      default: () => [],
    },
  },
  data() {
    return {
      param: {
        labels: [],
        datasets: [
          {
            label: 'Value',
            backgroundColor: '#C3FFA5',
            color: '#000',
            data: [],
            borderRadius: 10,
            borderSkipped: false,
          },
          {
            label: 'Not value',
            backgroundColor: '#F2A582',
            data: [],
            borderRadius: 10,
          },
        ],
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        scales: {
          x: {
            stacked: true,
          },
          y: {
            stacked: true,
          },
        },
      },
    };
  },
  computed: {
    ...mapState(['statsForGraph']),
  },
  created() {
    for (let author of this.statsForGraph) {
      this.param.labels.push(author.name)
      this.param.datasets[0].data.push(author.value)
      this.param.datasets[1].data.push(author.notValue)
    }
    console.log(this.param.labels)
  },
};
</script>
