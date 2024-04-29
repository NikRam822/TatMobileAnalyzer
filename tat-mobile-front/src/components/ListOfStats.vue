<template>
    <v-btn @click="getChurnStatistic()">
        Churn statistic
    </v-btn>
</template>
<script>
export default {
    methods: {
        getChurnStatistic() {
            let stats = []
            const statsRepo = this.$store.state.RepoSatistic[this.$store.state.currentRepo.projectLink].data
            for (let key in statsRepo.churn) {
                stats.push({ name: key, churn: Math.round(statsRepo.churn[key]) })
                stats[stats.length - 1].overall = statsRepo.overall[key]
                stats[stats.length - 1].total = Math.round(stats[stats.length - 1].overall * stats[stats.length - 1].churn / 100)
            }
            this.$store.commit("pushStats", stats)
        }
    }
}
</script>