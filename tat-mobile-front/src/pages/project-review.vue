<template>
  <component :is="currentPage"></component>
</template>

<script>
import noStatistics from "../components/statistics/noStatistics.vue";
import CocomoStatistics from "../components/statistics/CocomoStatistics.vue";
import ChurnStatistics from "../components/statistics/ChurnStatistics.vue";

export default {
  components: {
    CocomoStatistics,
    ChurnStatistics,
    noStatistics,
  },
  computed: {
    currentPage() {
      if (this.$store.state.RepoSatistic[this.$store.state.currentRepo.projectLink]) {
        return this.$store.state.currentStatisitc;
      } else {
        return "noStatistics";
      }
    },
  },
};
</script>
