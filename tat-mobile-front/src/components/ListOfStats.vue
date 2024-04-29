<template>
    <v-card>
        <v-list>
            <v-list-item v-for="(val) in statistics"><v-btn @click='executeFunction(val)'> {{ val
                    }} </v-btn></v-list-item>
        </v-list>
    </v-card>

    <v-table v-if="showTable">
        <thead>
            <tr>
                <th> Name </th>
                <th> Overall </th>
                <th> Churn </th>
                <th> Total </th>
            </tr>
        </thead>
        <tbody>
            <tr v-for="autor in this.$store.state.stats">
                <td><v-btn>{{ autor.name }}</v-btn></td>
                <td>{{ autor.overall }}</td>
                <td>{{ autor.churn }}</td>
                <td>{{ autor.total }}</td>
            </tr>
        </tbody>
    </v-table>
</template>
<script>
export default {
    data: () => ({
        currentStat: '',
        showTable: false,
        // Это список содержащий различные статистики (Пока что одна только раьботает)
        statistics: ["Churn Statistics", "BlaBLALFA", "Statistica"]
    }),
    methods: {
        // Функция для выбора нужной функции для получения статистики
        executeFunction(option) {
            this.currentStat = option
            switch (option) {
                case 'Churn Statistics':
                    return this.getChurnStatistic();
                case 'function2':
                    return function2();
                case 'function3':
                    return function3();
                default:
                    return "Invalid option";
            }
        },
        getChurnStatistic() {
            let stats = []
            const statsRepo = this.$store.state.RepoSatistic[this.$store.state.currentRepo.projectLink].data
            for (let key in statsRepo.churn) {
                stats.push({ name: key, churn: Math.round(statsRepo.churn[key]) })
                stats[stats.length - 1].overall = statsRepo.overall[key]
                stats[stats.length - 1].total = Math.round(stats[stats.length - 1].overall * stats[stats.length - 1].churn / 100)
            }
            this.showTable = true
            this.$store.commit("pushStats", stats)
        }
    }
}
</script>