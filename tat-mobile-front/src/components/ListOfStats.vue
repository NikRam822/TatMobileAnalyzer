<template>
    <v-card>
        <v-list>
            <v-list-item v-for="(val) in statistics" @click='executeFunction(val)'> {{ val
                }} <v-divider></v-divider></v-list-item>
        </v-list>
    </v-card>

</template>
<script>
export default {
    data: () => ({
        // Это список содержащий различные статистики (Пока что одна только раьботает)
        statistics: ["Churn Statistics", "BlaBLALFA", "Statistica"]
    }),
    methods: {
        executeFunction(val) {
            this.$store.commit("changeCurrentStatistic", val);
        }
    },
    created() {
        this.$store.commit("changeCurrentStatistic", "");
    }
}
</script>