<template>
    <v-btn @click="getStatistic(this.$store.state.currentRepo)">
        Churn statistic
    </v-btn>
</template>
<script>
export default {
    methods: {
        getStatistic(repo) {
        }
    }
}
</script>