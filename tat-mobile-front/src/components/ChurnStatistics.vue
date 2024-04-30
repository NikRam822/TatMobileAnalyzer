<template>
    <v-row style="height: 150px;">
        <v-col cols="4">
            <v-table>
                <thead>
                    <tr>
                        <th> Name </th>
                        <th> Overall </th>
                        <th> Churn </th>
                        <th> Value </th>
                        <th> Not value</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="autor in statsForGraph">
                        <td>{{ autor.name }}</td>
                        <td>{{ autor.overall }}</td>
                        <td>{{ autor.churn }}%</td>
                        <td>{{ autor.value }}</td>
                        <td>{{ autor.notValue }}</td>
                    </tr>
                </tbody>
            </v-table>
        </v-col>
        <v-col cols="8">
            <Bar />
        </v-col>
    </v-row>
</template>
<script>
import { mapState } from 'vuex';
export default {

    methods: {
        getChurnStatistic() {
            const statsRepo = this.$store.state.RepoSatistic[this.$store.state.currentRepo.projectLink].data
            let statsForGraph = []
            for (let name in statsRepo.churn) {
                let churn = statsRepo.churn[name]
                let overall = statsRepo.overall[name]
                statsForGraph.push({ name: name })
                statsForGraph[statsForGraph.length - 1].churn = Math.round(churn)
                statsForGraph[statsForGraph.length - 1].overall = overall
                statsForGraph[statsForGraph.length - 1].value = Math.round(overall * (100 - churn) / 100)
                statsForGraph[statsForGraph.length - 1].notValue = Math.round(overall * churn / 100)
            }
            this.$store.commit("changestatsForGraph", statsForGraph);
        }
    },
    created() {
        this.getChurnStatistic()
    },
    computed: {
        ...mapState(['statsForGraph']),
    },
}
</script>