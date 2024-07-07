<template>
  <div class="d-flex flex-column flex-md-row w-100">
    <v-table fixed-header>
      <thead>
        <tr>
          <th>
            <v-card id="name" @click="sortStatistic(statsForGraph, $event.target.id)"> Name</v-card>
          </th>
          <th>
            <v-card id="overall" @click="sortStatistic(statsForGraph, $event.target.id)"> Overall</v-card>
          </th>
          <th>
            <v-card id="churn" @click="sortStatistic(statsForGraph, $event.target.id)">Churn</v-card>
          </th>
          <th>
            <v-card id="value" @click="sortStatistic(statsForGraph, $event.target.id)">Value</v-card>
          </th>
          <th>
            <!-- rename not vlue -->
            <v-card id="notValue" @click="sortStatistic(statsForGraph, $event.target.id)">Not value</v-card>
          </th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="author in statsForGraph" :key="author.projectLink" @click="author.enable = !author.enable">
          <td>
            <div class="d-flex flex-row">
              <div class="align-self-center">
                <v-checkbox-btn density="compact" true-icon="mdi-eye" false-icon="mdi-eye-off" v-model="author.enable">
                </v-checkbox-btn>
              </div>
              <div class="align-self-center">{{ author.name }}</div>
            </div>
          </td>
          <td>{{ author.overall }}</td>
          <td>{{ author.churn }}%</td>
          <td>{{ author.value }}</td>
          <td>{{ author.notValue }}</td>
        </tr>
      </tbody>
    </v-table>
    <div
      :style="{
        height: 70 * statsForGraph.reduce((sum, curr) => sum + curr.enable, 0) + 60 + 'px',
        minWidth: 50 + '%',
      }"
    >
      <ChurnBar :statsForGraph="statsForGraph" />
    </div>
  </div>
</template>
<script>
export default {
  data() {
    return {
      prevCol: "",
      statsForGraph: this.getChurnStatistic(),
    };
  },

  methods: {
    sortStatistic(dataStat, sortParam) {
      if (this.prevCol != sortParam) {
        if (typeof dataStat[0][sortParam] == "string") {
          dataStat.sort(function (a, b) {
            return b[sortParam].localeCompare(a[sortParam]);
          });
        } else if (typeof dataStat[0][sortParam] == "number") {
          dataStat.sort(function (a, b) {
            return b[sortParam] - a[sortParam];
          });
        }
      } else {
        dataStat.reverse();
      }
      this.prevCol = sortParam;
    },
    getChurnStatistic() {
      const statsRepo = this.$store.state.RepoSatistic[this.$store.state.currentRepo.projectLink].data;
      const statsForGraph = [];
      for (let name in statsRepo.churn) {
        let churn = statsRepo.churn[name];
        let overall = statsRepo.overall[name];
        statsForGraph.push({ name: name });
        statsForGraph[statsForGraph.length - 1].churn = Math.round(churn);
        statsForGraph[statsForGraph.length - 1].overall = overall;
        statsForGraph[statsForGraph.length - 1].value = Math.round(overall * ((100 - churn) / 100));
        statsForGraph[statsForGraph.length - 1].notValue = Math.round((overall * churn) / 100);
        statsForGraph[statsForGraph.length - 1].enable = true;
      }
      return statsForGraph;
    },
  },
  created() {
    this.$store.watch(
      () => this.$store.state.RepoSatistic,
      () => {
        this.statsForGraph = this.getChurnStatistic();
      },
      { deep: true }
    );
  },
};
</script>
