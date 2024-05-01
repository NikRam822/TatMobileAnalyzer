<template>
  <!-- Как работает скролл:
  Параметр responsive в options задает размер графика в зависимости от родителя
По этому создаем div вокруг Bar и задаем ему ширину
В даннром случае это widthOfOneBar умножить на количество столбцов. Параметр widthOfOneBar я добавл в data
Вокруг этого div создаем еще один уже правильного размера в рамках страницы и ставим overflow-x, который добавляет скролл-->
  <div style="overflow-x: auto;">
    <div
      :style="{ width: widthOfOneBar * Object.getOwnPropertyNames(this.$store.state.statsForGraph).length - 1 + 'px' }">
      <Bar :data="updateChart" :options="options" :chart-id="chartId" :dataset-id-key="datasetIdKey" :plugins="plugins"
        :css-classes="cssClasses" :styles="styles" :width="width" :height="height" />
    </div>
  </div>
</template>

<script>
import { Bar } from 'vue-chartjs';
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
      widthOfOneBar: 100,
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
    updateChart() {
      let data = {
        labels: [],
        datasets: [
          {
            label: 'Value',
            backgroundColor: '#C3FFA5',
            color: '#000',
            data: [],
            borderRadius: 5,
            borderSkipped: false,
            barPercentage: 0.5
          },
          {
            label: 'Not value',
            backgroundColor: '#F2A582',
            data: [],
            borderRadius: 5,
            barPercentage: 0.5
          },
        ],
      }
      data.labels = []
      data.datasets[0].data = []
      data.datasets[1].data = []
      for (let author of this.$store.state.statsForGraph) {
        if (author.enable) {
          data.labels.push(author.name)
          data.datasets[0].data.push(author.value)
          data.datasets[1].data.push(author.notValue)
        }
      }
      return data
    }
  }
}
</script>
