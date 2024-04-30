<template>
    <Bar
      :chart-options="chartOptions"
      :chart-data="chartData"
      :chart-id="chartId"
      :dataset-id-key="datasetIdKey"
      :plugins="plugins"
      :css-classes="cssClasses"
      :styles="styles"
      :width="width"
      :height="height"
    />
  </template>
  
  <script>
  import { Bar } from "vue-chartjs";
  
  import {
    Chart as ChartJS,
    Title,
    Tooltip,
    Legend,
    BarElement,
    CategoryScale,
    LinearScale,
  } from "chart.js";
  
  ChartJS.register(
    Title,
    Tooltip,
    Legend,
    BarElement,
    CategoryScale,
    LinearScale
  );
  
  export default {
    name: "BarChart",
    components: {
      Bar,
    },
    props: {
      chartId: {
        type: String,
        default: "bar-chart",
      },
      datasetIdKey: {
        type: String,
        default: "label",
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
        default: "",
        type: String,
      },
      styles: {
        type: Object,
        default: () => {},
      },
      plugins: {
        type: Array,
        default: () => [],
      },
    },
    data() {
      return {
        chartData: {
          labels: [
            "January",
            "February",
            "March",
            "April",
            "May",
            "June",
            "July",
            "August",
            "September",
            "October",
            "November",
            "December",
          ],
          datasets: [
            {
              label: "Data One",
              backgroundColor: "#C3FFA5",
              data: [15, 20, 12, 39, 10, 40, 39, 80, 40, 20, 12, 11],
              borderRadius: 10, // Добавлено свойство для закругления углов
              borderSkipped: false, // Добавлено свойство для отображения границ со всех сторон
            },
            {
              label: "Data Two",
              backgroundColor: "#F2A582",
              data: [10, 40, 30, 20, 50, 15, 30, 20, 10, 30, 40, 20],
              borderRadius: 10, // Добавлено свойство для закругления углов
            },
          ],
        },
        chartOptions: {
          responsive: true,
          maintainAspectRatio: false,
          scales: {
            x: {
              stacked: true, // Добавлено свойство для наложения столбцов друг на друга
            },
            y: {
              stacked: true, // Добавлено свойство для наложения столбцов друг на друга
            },
          },
        },
      };
    },
  };
  </script>
  