<template>
  <Bar :data="updateChart" :options="options" />
</template>

<script>
import { Bar } from "vue-chartjs";
import { Chart, Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale } from "chart.js";

Chart.register(Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale);
Chart.defaults.color = "#FFF";
export default {
  name: "BarChart",
  components: {
    Bar,
  },
  props: ["statsForGraph"],
  data() {
    return {
      options: {
        indexAxis: "y",
        responsive: true,
        maintainAspectRatio: false,
        scales: {
          x: {
            stacked: true,
            display: true,
            title: {
              display: true,
              text: "Overall",
            },
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
            label: "Value",
            backgroundColor: "#C3FFA5",
            color: "#000",
            data: [],
            borderRadius: 5,
            borderSkipped: false,
            barPercentage: 0.5,
          },
          {
            label: "Not value",
            backgroundColor: "#F2A582",
            data: [],
            borderRadius: 5,
            barPercentage: 0.5,
          },
        ],
      };
      data.labels = [];
      data.datasets[0].data = [];
      data.datasets[1].data = [];
      for (let author of this.statsForGraph) {
        if (author.enable) {
          data.labels.push(author.name);
          data.datasets[0].data.push(author.value);
          data.datasets[1].data.push(author.notValue);
        }
      }
      return data;
    },
  },
};
</script>
