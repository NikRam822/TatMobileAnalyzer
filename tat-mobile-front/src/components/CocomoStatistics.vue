<template>
  <div class="d-flex">
    <div class="d-flex flex-column justify-space-around">
      <v-sheet width="150px" class="text-center ma-2">
        <p>Your team type:</p>
        <v-select v-model="currentTeam" :items="teamType" variant="solo-inverted" hide-details="true"> </v-select>
      </v-sheet>
      <v-sheet width="150px" class="text-center ma-2">
        <p>Lines of code:</p>
        <v-text-field variant="solo-inverted" @input="validateInput" v-model="LOC"></v-text-field>
      </v-sheet>
    </div>
    <div class="d-flex flex-row justify-space-around ma-2" style="width: 100%">
      <v-sheet class="text-center">
        <p style="color: rgb(197, 226, 21)">Labor intensity with risk</p>
        <v-chip size="large">
          {{ laborIntensityWithRisk }}
        </v-chip>
      </v-sheet>
      <v-sheet class="text-center">
        <p>Person-months</p>
        <v-chip>
          {{ personMonths }}
        </v-chip>
      </v-sheet>
      <v-sheet class="text-center">
        <p>Months</p>
        <v-chip> {{ months }}</v-chip>
      </v-sheet>
      <v-sheet class="text-center">
        <p>Personnel</p>
        <v-chip> {{ personel }}</v-chip>
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
            @click="(param.pick = param.vl), updateResult()"
            v-model="param.pick"
            true-value="vl"
            :label="String(param.vl)"
          ></v-radio>
        </td>
        <td>
          <v-radio
            v-if="param.l"
            @click="(param.pick = param.l), updateResult()"
            v-model="param.pick"
            true-value="l"
            :label="String(param.l)"
          ></v-radio>
        </td>
        <td>
          <v-radio
            v-if="param.n"
            @click="(param.pick = param.n), updateResult()"
            v-model="param.pick"
            true-value="n"
            :label="String(param.n)"
          ></v-radio>
        </td>
        <td>
          <v-radio
            v-if="param.h"
            @click="(param.pick = param.h), updateResult()"
            v-model="param.pick"
            true-value="h"
            :label="String(param.h)"
          ></v-radio>
        </td>
        <td>
          <v-radio
            v-if="param.vh"
            @click="(param.pick = param.vh), updateResult()"
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
      teamType: ["ORGANIC", "SEMIDETACTCH", "EMBEDDED"],
      currentTeam: "ORGANIC",
      LOC: 0,
      personMonths: 0,
      months: 0,
      personel: 0,
      laborIntensityWithRisk: 0,
      table: [
        {
          pick: "n",
          name1: "analyticalSkills",
          name: "Required Software Reliability",
          vl: 0.75,
          l: 0.88,
          n: 1.0,
          h: 1.15,
          vh: 1.4,
        },
        {
          pick: "n",
          name1: "databaseSize",
          name: "Size of Application Database",
          vl: 0,
          l: 0.94,
          n: 1.0,
          h: 1.08,
          vh: 1.16,
        },
        {
          pick: "n",
          name1: "developmentExperience",
          name: "Complexity of The Product",
          vl: 0.7,
          l: 0.85,
          n: 1.0,
          h: 1.15,
          vh: 1.3,
        },
        {
          pick: "n",
          name1: "developmentMethods",
          name: "Runtime Performance Constraints",
          vl: 0,
          l: 0,
          n: 1.0,
          h: 1.11,
          vh: 1.3,
        },
        { pick: "n", name1: "developmentSchedule", name: "Memory Constraints", vl: 0, l: 0, n: 1.0, h: 1.06, vh: 1.21 },
        {
          pick: "n",
          name1: "developmentSkills",
          name: "Volatility of the virtual machine environment",
          vl: 0,
          l: 0.87,
          n: 1.0,
          h: 1.15,
          vh: 1.3,
        },
        {
          pick: "n",
          name1: "developmentTools",
          name: "Required turnaround time",
          vl: 0,
          l: 0.94,
          n: 1.0,
          h: 1.07,
          vh: 1.15,
        },
        {
          pick: "n",
          name1: "languageExperience",
          name: "Analyst capability",
          vl: 1.46,
          l: 1.19,
          n: 1.0,
          h: 0.86,
          vh: 0.71,
        },
        {
          pick: "n",
          name1: "memoryLimit",
          name: "Applications experience",
          vl: 1.29,
          l: 1.13,
          n: 1.0,
          h: 0.91,
          vh: 0.82,
        },
        {
          pick: "n",
          name1: "performance",
          name: "Software engineer capability",
          vl: 1.42,
          l: 1.17,
          n: 1.0,
          h: 0.86,
          vh: 0.7,
        },
        {
          pick: "n",
          name1: "productComplexity",
          name: "Virtual machine experience",
          vl: 1.21,
          l: 1.1,
          n: 1.0,
          h: 0.9,
          vh: 0,
        },
        {
          pick: "n",
          name1: "recoveryTime",
          name: "Programming language experience",
          vl: 1.14,
          l: 1.07,
          n: 1.0,
          h: 0.95,
          vh: 0,
        },
        {
          pick: "n",
          name1: "reliability",
          name: "Application of software engineering methods",
          vl: 1.24,
          l: 1.1,
          n: 1.0,
          h: 0.91,
          vh: 0.82,
        },
        {
          pick: "n",
          name1: "unstableEnvironment",
          name: "Use of software tools",
          vl: 1.24,
          l: 1.1,
          n: 1.0,
          h: 0.91,
          vh: 0.83,
        },
        {
          pick: "n",
          name1: "virtualMachinesExperience",
          name: "Required development schedule",
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
    validateInput(event) {
      const value = event.target.value;
      if (/^\d*$/.test(value)) {
        this.number = value;
      } else {
        event.target.value = this.number;
      }
    },
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
  },
  computed: {
    calculateLOC() {
      let totalValue = 0;
      for (let val of Object.values(
        this.$store.state.RepoSatistic[this.$store.state.currentRepo.projectLink].data.overall
      )) {
        totalValue += val;
      }
      totalValue = totalValue;
      return totalValue / 1000;
    },
  },
  mounted() {
    this.LOC = this.calculateLOC;
    this.updateResult();
  },
};
</script>
