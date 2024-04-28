<template>
  <p> {{ repos }}</p>
</template>

<script>
export default {
  data: () => ({
  }),
  created() {
    this.repos = this.$route.query.repoName;
  }
}
</script>
