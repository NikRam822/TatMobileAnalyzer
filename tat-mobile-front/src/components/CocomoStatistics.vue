<template>
  <div class="d-flex">
    <div class="d-flex flex-column justify-space-around">
      <v-sheet width="150px" class="text-center ma-2">
        <p>Your team type:</p>
        <v-select v-model="currentTeam" :items="teamType" variant="solo-inverted" hide-details="true"> </v-select>
      </v-sheet>
      <v-sheet width="150px" class="text-center ma-2">
        <p>Lines of code:</p>
        <v-text-field
          type="number"
          variant="solo-inverted"
          v-model="LOC"
          min="0"
          append-inner-icon="mdi-autorenew"
          @click:append-inner="this.LOC = calculateLOC()"
          hint="enter the number of thousand lines"
          suffix="k"
        ></v-text-field>
      </v-sheet>
    </div>
    <div class="d-flex flex-row justify-space-around ma-2 mt-16" style="width: 100%">
      <v-sheet class="text-center">
        <p style="color: rgb(197, 226, 21)">Labor intensity with risk</p>
        <v-chip size="x-large">
          {{ laborIntensityWithRisk }}
        </v-chip>
      </v-sheet>
      <v-sheet class="text-center">
        <p>Person-months</p>
        <v-chip size="large">
          {{ personMonths }}
        </v-chip>
      </v-sheet>
      <v-sheet class="text-center">
        <p>Months</p>
        <v-chip size="large"> {{ months }}</v-chip>
      </v-sheet>
      <v-sheet class="text-center">
        <p>Personnel</p>
        <v-chip size="large"> {{ personel }}</v-chip>
      </v-sheet>
    </div>
  </div>
  <v-table>
    <thead>
      <tr>
        <th>Cost Drivers</th>
        <th>Very low</th>
        <th>Low</th>
        <th>Nominal</th>
        <th>High</th>
        <th>Very high</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="param in table" :key="param.name">
        <td>{{ param.name }}</td>
        <td>
          <v-radio
            v-if="param.vl"
            @click="
              param.pick = 'vl';
              updateResult();
            "
            v-model="param.pick"
            true-value="vl"
            :label="String(param.vl)"
          ></v-radio>
        </td>
        <td>
          <v-radio
            v-if="param.l"
            @click="
              param.pick = 'l';
              updateResult();
            "
            v-model="param.pick"
            true-value="l"
            :label="String(param.l)"
          ></v-radio>
        </td>
        <td>
          <v-radio
            v-if="param.n"
            @click="
              param.pick = 'n';
              updateResult();
            "
            v-model="param.pick"
            true-value="n"
            :label="String(param.n)"
          ></v-radio>
        </td>
        <td>
          <v-radio
            v-if="param.h"
            @click="
              param.pick = 'h';
              updateResult();
            "
            v-model="param.pick"
            true-value="h"
            :label="String(param.h)"
          ></v-radio>
        </td>
        <td>
          <v-radio
            v-if="param.vh"
            @click="
              param.pick = 'vh';
              updateResult();
            "
            v-model="param.pick"
            true-value="vh"
            :label="String(param.vh)"
          ></v-radio>
        </td>
      </tr>
    </tbody>
  </v-table>
</template>
<script>
import axios from "axios";
let server_path = import.meta.env.VITE_BACKEND_URL;
export default {
  data() {
    return {
      teamType: ["ORGANIC", "SEMIDETACH", "EMBEDDED"],
      currentTeam: "ORGANIC",
      LOC: 0,
      personMonths: 0,
      months: 0,
      personel: 0,
      laborIntensityWithRisk: 0,
      table: [
        {
          pick: "n",
          name1: "reliability",
          name: "Reliability",
          vl: 0.75,
          l: 0.88,
          n: 1.0,
          h: 1.15,
          vh: 1.4,
        },
        {
          pick: "n",
          name1: "databaseSize",
          name: "Database Size",
          vl: 0,
          l: 0.94,
          n: 1.0,
          h: 1.08,
          vh: 1.16,
        },
        {
          pick: "n",
          name1: "productComplexity",
          name: "Product Complexity",
          vl: 0.7,
          l: 0.85,
          n: 1.0,
          h: 1.15,
          vh: 1.3,
        },
        {
          pick: "n",
          name1: "performance",
          name: "Performance",
          vl: 0,
          l: 0,
          n: 1.0,
          h: 1.11,
          vh: 1.3,
        },
        { pick: "n", name1: "memoryLimit", name: "Memory limit", vl: 0, l: 0, n: 1.0, h: 1.06, vh: 1.21 },
        {
          pick: "n",
          name1: "unstableEnvironment",
          name: "Unstable environment",
          vl: 0,
          l: 0.87,
          n: 1.0,
          h: 1.15,
          vh: 1.3,
        },
        {
          pick: "n",
          name1: "recoveryTime",
          name: "Recovery time",
          vl: 0,
          l: 0.94,
          n: 1.0,
          h: 1.07,
          vh: 1.15,
        },
        {
          pick: "n",
          name1: "analyticalSkills",
          name: "Analytical skill",
          vl: 1.46,
          l: 1.19,
          n: 1.0,
          h: 0.86,
          vh: 0.71,
        },
        {
          pick: "n",
          name1: "developmentSkills",
          name: "Development skills",
          vl: 1.29,
          l: 1.13,
          n: 1.0,
          h: 0.91,
          vh: 0.82,
        },
        {
          pick: "n",
          name1: "developmentExperience",
          name: "Development experience",
          vl: 1.42,
          l: 1.17,
          n: 1.0,
          h: 0.86,
          vh: 0.7,
        },
        {
          pick: "n",
          name1: "virtualMachinesExperience",
          name: "Virtual machine experience",
          vl: 1.21,
          l: 1.1,
          n: 1.0,
          h: 0.9,
          vh: 0,
        },
        {
          pick: "n",
          name1: "languageExperience",
          name: "language experience",
          vl: 1.14,
          l: 1.07,
          n: 1.0,
          h: 0.95,
          vh: 0,
        },
        {
          pick: "n",
          name1: "developmentTools",
          name: "Development tools",
          vl: 1.24,
          l: 1.1,
          n: 1.0,
          h: 0.91,
          vh: 0.82,
        },
        {
          pick: "n",
          name1: "developmentMethods",
          name: "Development methods",
          vl: 1.24,
          l: 1.1,
          n: 1.0,
          h: 0.91,
          vh: 0.83,
        },
        {
          pick: "n",
          name1: "developmentSchedule",
          name: "Development schedule",
          vl: 1.23,
          l: 1.08,
          n: 1.0,
          h: 1.04,
          vh: 1.1,
        },
      ],
      test: true,
    };
  },
  methods: {
    async updateResult() {
      const resp = {};
      for (let item of this.table) {
        resp[item.name1] = item[item.pick];
      }
      resp.kloc = this.LOC;
      resp.projectType = this.currentTeam;
      let hostadress = server_path + "/api/cocomo/calculate";
      try {
        let response = await axios.post(hostadress, resp);
        response = response.data;
        this.personMonths = response.personMonths;
        this.months = response.months;
        this.personel = response.personnel;
        this.laborIntensityWithRisk = response.laborIntensityWithRisk;
      } catch (error) {
        console.error("Error: ", error);
      }
    },
    calculateLOC() {
      let totalValue = 0;
      const statsRepo = this.$store.state.RepoSatistic[this.$store.state.currentRepo.projectLink].data;
      for (let name in statsRepo.churn) {
        let churn = statsRepo.churn[name];
        let overall = statsRepo.overall[name];
        totalValue += Math.round((overall * (100 - churn)) / 100);
      }
      return totalValue / 1000;
    },
  },
  computed: {},
  mounted() {
    this.LOC = this.calculateLOC();
    this.updateResult();
  },
  watch: {
    LOC() {
      this.updateResult();
    },
    currentTeam() {
      this.updateResult();
    },
  },
};
</script>
