<template>
    <v-row>
        <v-col cols="4">
            <v-table height="80vh">
                <thead>
                    <tr>
                        <th><v-card id="name" @click="sortStatistic(statsForGraph, $event.target.id)"> Name</v-card>
                        </th>
                        <th><v-card id="overall" @click="sortStatistic(statsForGraph, $event.target.id)">
                                Overall</v-card> </th>
                        <th> <v-card id="churn" @click="sortStatistic(statsForGraph, $event.target.id)">Churn</v-card>
                        </th>
                        <th> <v-card id="value" @click="sortStatistic(statsForGraph, $event.target.id)">Value</v-card>
                        </th>
                        <th> <v-card id="notValue" @click="sortStatistic(statsForGraph, $event.target.id)">Not
                                value</v-card></th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="autor in statsForGraph">
                        <td> <v-checkbox true-icon="mdi-eye" false-icon="mdi-eye-off" :label="autor.name"
                                v-model="autor.enable"></v-checkbox></td>
                        <td>{{ autor.overall }}</td>
                        <td>{{ autor.churn }}%</td>
                        <td>{{ autor.value }}</td>
                        <td>{{ autor.notValue }}</td>
                    </tr>
                </tbody>
            </v-table>
        </v-col>
        <v-col cols="8">
            <div>
                <Bar />
            </div>
        </v-col>
    </v-row>
</template>
<script>
import { mapState } from 'vuex';
export default {
    data: () => ({
        prevCol: ''
    }),
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
                statsForGraph[statsForGraph.length - 1].enable = true
            }
            this.$store.commit("changestatsForGraph", statsForGraph);
        },
        sortStatistic(dataStat, sortParam) {
            if (this.prevCol != sortParam) {
                if (typeof dataStat[0][sortParam] == "string") {
                    dataStat.sort(function (a, b) { return b[sortParam].localeCompare(a[sortParam]) })
                } else if (typeof dataStat[0][sortParam] == "number") {
                    dataStat.sort(function (a, b) { return b[sortParam] - a[sortParam] })
                }
            } else {
                dataStat.reverse()
            }
            this.prevCol = sortParam
        },
    },
    created() {
        this.getChurnStatistic()
    },
    computed: {
        ...mapState(['statsForGraph']),
    },
}
</script>