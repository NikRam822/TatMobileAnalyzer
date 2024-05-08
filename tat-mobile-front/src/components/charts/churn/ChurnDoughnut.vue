<template>
  <Doughnut :data="updateChart" :options="options" />
</template>

<script lang="ts">
import { Chart, ArcElement, Tooltip, Legend } from "chart.js";
import { Doughnut } from "vue-chartjs";
import { Colors } from "chart.js";

Chart.register(ArcElement, Tooltip, Legend, Colors);
Chart.defaults.color = "#FFF";
export default {
  name: "App",
  components: {
    Doughnut,
  },
  props: ["statsForGraph"],
  data() {
    return {
      options: {
        plugins: {
          title: {
            display: true,
            text: "Value",
          },
        },
        responsive: true,
        maintainAspectRatio: false,
      },
    };
  },
  computed: {
    updateChart() {
      let data = {
        labels: [],
        datasets: [
          {
            data: [],
          },
        ],
      };
      let stats = this.statsForGraph.toSorted((a, b) => {
        return a.value - b.value;
      });
      for (let key in stats) {
        if (stats[key].enable) {
          if (key < 5) {
            data.labels.push(stats[key].name);
            data.datasets[0].data.push(stats[key].value);
          } else {
            data.labels[5] = "other";
            data.datasets[0].data[5] += stats[key].value;
          }
        }
      }
      return data;
    },
  },
};
</script>
