<template>
  <component :is="currentPage"></component>
</template>

<script>
import CocomoStatistics from "../components/CocomoStatistics.vue";
import ChurnStatistics from "../components/ChurnStatistics.vue";

export default {
  components: {
    CocomoStatistics,
    ChurnStatistics,
  },
  computed: {
    currentPage() {
      return this.$store.state.currentStatisitc;
    },
  },
};
</script>
