<template>
    <v-btn @click="getChurnStatistic(this.$store.state.currentRepo)">
        Churn statistic
    </v-btn>
</template>
<script>
export default {
    methods: {
        getChurnStatistic(repo) {
        }
    }
}
</script>