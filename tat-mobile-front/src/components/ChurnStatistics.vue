<template>
    <v-row>
        <v-col cols="4">
            <v-table height="80vh" fixed-header>
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
                        <td>
                            <div class="d-flex flex-row">
                                <div class="align-self-center">
                                    <v-checkbox-btn density="compact" true-icon="mdi-eye" false-icon="mdi-eye-off"
                                        v-model="autor.enable">
                                    </v-checkbox-btn>
                                </div class=align-self-center>
                                <div class="align-self-center">{{ autor.name }}</div>
                            </div>
                        </td>
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
    data: () => ({
        prevCol: ''
    }),
    methods: {
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
    computed: {
        ...mapState(['statsForGraph']),
    },
}
</script>