<template>
    <Bar/>
    <!-- <v-table>
        <thead>
            <tr>
                <th> Name </th>
                <th> Overall </th>
                <th> Churn </th>
                <th> Total </th>
            </tr>
        </thead>
        <tbody>
            <tr v-for="autor in stats">
                <td>{{ autor.name }}</td>
                <td>{{ autor.overall }}</td>
                <td>{{ autor.churn }}</td>
                <td>{{ autor.total }}</td>
            </tr>
        </tbody>
    </v-table> -->
</template>
<script>

export default {
    data: () => ({
        stats: []
    }),
    methods: {
        getChurnStatistic() {
            const statsRepo = this.$store.state.RepoSatistic[this.$store.state.currentRepo.projectLink].data
            for (let key in statsRepo.churn) {
                this.stats.push({ name: key, churn: Math.round(statsRepo.churn[key]) })
                this.stats[this.stats.length - 1].overall = statsRepo.overall[key]
                this.stats[this.stats.length - 1].total = Math.round(this.stats[this.stats.length - 1].overall * this.stats[this.stats.length - 1].churn / 100)
            }
        }
    },
    created() {
        this.getChurnStatistic()
    }
}
</script>