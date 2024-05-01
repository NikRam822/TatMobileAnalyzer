<template>
    <v-card class="d-flex flex-column">
        <v-list>
            <v-list-item v-for="(val) in statistics" @click='executeFunction(val)'> {{ val
                }} <v-divider></v-divider></v-list-item>
        </v-list>

        <v-list v-for="(files, filter) in this.$store.state.filters">
            <v-list-item>
                <v-menu open-on-hover>
                    <template v-slot:activator="{ props }">
                        <v-btn width="100%" variant="outlined" @click="updateStatistic(this.$store.state.currentRepo)"
                            v-bind="props">
                            {{ filter }}
                        </v-btn>
                    </template>
                    <v-list>
                        <v-list-item v-for="(file, id) in files">
                            <v-card width="100%" class="d-flex flex-wrap">
                                {{ file }}
                                <v-spacer></v-spacer>
                                <v-icon @click="deliteFile(filter, id)" icon="mdi-close" class="flex-2-0"></v-icon>
                                <v-divider class="flex-1-1-100"></v-divider>
                            </v-card>
                        </v-list-item>
                        <v-list-item>
                            <v-btn width="100%">
                                <v-icon icon="mdi-plus"></v-icon>
                                <v-overlay activator="parent">
                                    <v-card class="ma-10" width="400px">
                                        <v-form @submit.prevent="addFile(filter)">
                                            <v-autocomplete required label="Enter file path" v-model="path"
                                                :items="Object.getOwnPropertyNames(this.$store.state.RepoSatistic[this.$store.state.currentRepo.projectLink].data.general)"></v-autocomplete>
                                            <v-btn type="submit" width="100%"> Add file </v-btn>
                                        </v-form>
                                    </v-card>
                                </v-overlay>
                            </v-btn>
                        </v-list-item>
                    </v-list>
                </v-menu>
            </v-list-item>
        </v-list>

        <v-progress-circular v-if="loader" size="100" width="20" class="align-self-center"
            indeterminate></v-progress-circular>
    </v-card>
</template>
<script>
import axios from 'axios'
export default {
    data: () => ({
        // Это список содержащий различные статистики (Пока что одна только раьботает)
        statistics: ["Churn Statistics", "Work in progress Statistic", "Work in progress Statistic"],
        loader: false,
        path: ''

    }),
    methods: {
        async updateFilters() {
            let hostadress = `http://localhost:8080/filter/get-filters-for-project?projectId=${this.$store.state.currentRepo.projectId}`
            try {
                const filters = await axios.get(hostadress)
                this.$store.commit('changeFilter', filters.data)
            } catch (error) {
                console.error('Error: ', error);
            }

        },
        async updateStatistic(repo) {
            this.loader = true
            let hostadress = "http://localhost:8080/api/patch/statistic"
            try {
                const statistic = await axios.post(hostadress, {
                    projectId: repo.projectId,
                    projectLink: repo.projectLink,
                    projectName: repo.projectName,
                });
                this.$store.commit("addStatistc", [repo.projectLink, statistic])
            } catch (error) {
                console.error("Error " + error.message);
            }
            this.getChurnStatistic()
            this.loader = false
        },
        async addFile(filt) {
            let filts = this.$store.state.filters
            filts[filt].splice(filts[filt].length - 1, 0, this.path)
            this.$store.commit('changeFilter', filts)
            let hostadress = `http://localhost:8080/filter/update-filter?projectId=${this.$store.state.currentRepo.projectId}`
            try {
                await axios.put(hostadress, filts);
            } catch (error) {
                console.error("Error " + error.message);
            }
            this.path = ''
        },
        async deliteFile(filt) {
            let filts = this.$store.state.filters
            filts[filt].splice(0, 1)
            this.$store.commit('changeFilter', filts)
            let hostadress = `http://localhost:8080/filter/update-filter?projectId=${this.$store.state.currentRepo.projectId}`
            try {
                await axios.put(hostadress, filts);
            } catch (error) {
                console.error("Error " + error.message);
            }
        },
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
    },
    created() {
        this.updateFilters()
        this.getChurnStatistic()
    }
}
</script>