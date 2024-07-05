<template>
  <div class="d-flex">
    <div class="d-flex flex-column justify-space-around">
      <v-sheet width="150px" class="text-center ma-2">
        <p>Your team type:</p>
        <v-select v-model="currentTeam" :items="teamTeamType" variant="solo-inverted" hide-details="true"> </v-select>
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

  <v-divider class="pb-7"></v-divider>

  <div class="d-flex flex-row px-2">
    <v-btn
      variant="outlined"
      @click="
        resetButton();
        updateResult();
      "
    >
      Reset Coefficients <v-icon icon="mdi-autorenew"></v-icon
    ></v-btn>
    <v-spacer></v-spacer>
    <v-btn variant="outlined" @click="currentCoefMode = Math.abs(currentCoefMode - 1)">
      {{ modes[currentCoefMode] }}
    </v-btn>
  </div>

  <v-table v-if="currentCoefMode === 0">
    <thead>
      <tr>
        <th width="30%">Cost Drivers</th>
        <th>Very low</th>
        <th>Low</th>
        <th>Nominal</th>
        <th>High</th>
        <th>Very high</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="param in table" :key="param.name">
        <td>
          <v-expansion-panels flat static>
            <v-expansion-panel>
              <v-expansion-panel-title>
                {{ param.name }}
              </v-expansion-panel-title>
              <v-expansion-panel-text>
                {{ param.description }}
              </v-expansion-panel-text>
            </v-expansion-panel>
          </v-expansion-panels>
        </td>
        <td>
          <v-radio
            v-if="param.coefficient.vl"
            @click="
              coefficient[param.codeName] = { simpleCoef: 'vl', coef: param.coefficient.vl };
              updateResult();
            "
            v-model="coefficient[param.codeName].simpleCoef"
            true-value="vl"
          ></v-radio>
        </td>
        <td>
          <v-radio
            v-if="param.coefficient.l"
            @click="
              coefficient[param.codeName] = { simpleCoef: 'l', coef: param.coefficient.l };
              updateResult();
            "
            v-model="coefficient[param.codeName].simpleCoef"
            true-value="l"
          ></v-radio>
        </td>
        <td>
          <v-radio
            v-if="param.coefficient.n"
            @click="
              coefficient[param.codeName] = { simpleCoef: 'n', coef: param.coefficient.n };
              updateResult();
            "
            v-model="coefficient[param.codeName].simpleCoef"
            true-value="n"
          ></v-radio>
        </td>
        <td>
          <v-radio
            v-if="param.coefficient.h"
            @click="
              coefficient[param.codeName] = { simpleCoef: 'h', coef: param.coefficient.h };
              updateResult();
            "
            v-model="coefficient[param.codeName].simpleCoef"
            true-value="h"
          ></v-radio>
        </td>
        <td>
          <v-radio
            v-if="param.coefficient.vh"
            @click="
              coefficient[param.codeName] = { simpleCoef: 'vh', coef: param.coefficient.vh };
              updateResult();
            "
            v-model="coefficient[param.codeName].simpleCoef"
            true-value="vh"
          ></v-radio>
        </td>
      </tr>
    </tbody>
  </v-table>

  <v-table v-if="currentCoefMode === 1">
    <thead>
      <tr>
        <th width="30%">Cost Drivers</th>
        <th>Coefficients</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="param in table" :key="param.name">
        <td>
          <v-expansion-panels flat static>
            <v-expansion-panel>
              <v-expansion-panel-title>
                {{ param.name }}
              </v-expansion-panel-title>
              <v-expansion-panel-text>
                {{ param.description }}
              </v-expansion-panel-text>
            </v-expansion-panel>
          </v-expansion-panels>
        </td>
        <td>
          <div class="d-flex flex-row">
            <div>
              <v-text-field
                density="compact"
                @change="updateCoef(param.codeName)"
                type="number"
                variant="plain"
                v-model="coefficient[param.codeName].coef"
                min="0"
                max="2"
                step="0.01"
                hide-details
              ></v-text-field>
            </div>
            <v-slider
              min="0.5"
              max="1.5"
              step="0.01"
              thumb-label
              hide-details
              :ticks="Object.fromEntries(Object.entries(param.coefficient).map(([key, value]) => [value, key]))"
              show-ticks="always"
              tick-size="6"
              v-model="coefficient[param.codeName].coef"
              @click="updateCoef(param.codeName)"
            ></v-slider>
          </div>
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
      table: [
        {
          codeName: "reliability",
          name: "Reliability",
          description: "Required software reliability",
          coefficient: {
            vl: 0.75,
            l: 0.88,
            n: 1.0,
            h: 1.15,
            vh: 1.4,
          },
        },
        {
          codeName: "databaseSize",
          name: "Database Size",
          description: "Size of application database",
          coefficient: {
            l: 0.94,
            n: 1.0,
            h: 1.08,
            vh: 1.16,
          },
        },
        {
          codeName: "productComplexity",
          name: "Product Complexity",
          description: "Complexity of the product",
          coefficient: {
            vl: 0.7,
            l: 0.85,
            n: 1.0,
            h: 1.15,
            vh: 1.3,
          },
        },
        {
          codeName: "performance",
          name: "Performance",
          description: "Run-time performance constraints",
          coefficient: {
            n: 1.0,
            h: 1.11,
            vh: 1.3,
          },
        },
        {
          codeName: "memoryLimit",
          name: "Memory limit",
          description: "Memory constraints",
          coefficient: {
            n: 1.0,
            h: 1.06,
            vh: 1.21,
          },
        },
        {
          codeName: "unstableEnvironment",
          name: "Unstable environment",
          description: "Volatility of the virtual machine environment",
          coefficient: {
            l: 0.87,
            n: 1.0,
            h: 1.15,
            vh: 1.3,
          },
        },
        {
          codeName: "recoveryTime",
          name: "Recovery time",
          description: "Required turnabout time",
          coefficient: {
            l: 0.94,
            n: 1.0,
            h: 1.07,
            vh: 1.15,
          },
        },
        {
          codeName: "analyticalSkills",
          name: "Analytical skill",
          description: "Analyst capability",
          coefficient: {
            vl: 1.46,
            l: 1.19,
            n: 1.0,
            h: 0.86,
            vh: 0.71,
          },
        },
        {
          codeName: "developmentSkills",
          name: "Development skills",
          description: "Applications experience",
          coefficient: {
            vl: 1.29,
            l: 1.13,
            n: 1.0,
            h: 0.91,
            vh: 0.82,
          },
        },
        {
          codeName: "developmentExperience",
          name: "Development experience",
          description: "Software engineer capability",
          coefficient: {
            vl: 1.42,
            l: 1.17,
            n: 1.0,
            h: 0.86,
            vh: 0.7,
          },
        },
        {
          codeName: "virtualMachinesExperience",
          name: "Virtual machine experience",
          description: "Virtual machine experience",
          coefficient: {
            vl: 1.21,
            l: 1.1,
            n: 1.0,
            h: 0.9,
          },
        },
        {
          codeName: "languageExperience",
          name: "language experience",
          description: "Programming language experience",
          coefficient: {
            vl: 1.14,
            l: 1.07,
            n: 1.0,
            h: 0.95,
          },
        },
        {
          codeName: "developmentTools",
          name: "Development tools",
          description: "Application of software engineering methods",
          coefficient: {
            vl: 1.24,
            l: 1.1,
            n: 1.0,
            h: 0.91,
            vh: 0.82,
          },
        },
        {
          codeName: "developmentMethods",
          name: "Development methods",
          description: "Use of software tools",
          coefficient: {
            vl: 1.24,
            l: 1.1,
            n: 1.0,
            h: 0.91,
            vh: 0.83,
          },
        },
        {
          codeName: "developmentSchedule",
          name: "Development schedule",
          description: "Required development schedule",
          coefficient: {
            vl: 1.23,
            l: 1.08,
            n: 1.0,
            h: 1.04,
            vh: 1.1,
          },
        },
      ],
      teamTeamType: ["ORGANIC", "SEMIDETACH", "EMBEDDED"],
      currentTeam: "ORGANIC",
      LOC: 0,
      coefficient: {},
      personMonths: 0,
      months: 0,
      personel: 0,
      laborIntensityWithRisk: 0,
      modes: ["Go to extend mode", "Go to simple mode"],
      currentCoefMode: 0,
    };
  },
  methods: {
    updateCoef(name) {
      this.coefficient[name].simpleCoef = "pass";
      this.updateResult();
    },

    async updateResult() {
      this.$store.commit("setParams", { LOC: this.LOC, coefficient: this.coefficient, currentTeam: this.currentTeam });
      const resp = {};
      for (let item of this.table) {
        resp[item.codeName] = this.coefficient[item.codeName].coef;
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

    resetCoef() {
      const coef = {};
      this.table.forEach((item) => {
        coef[item.codeName] = { simpleCoef: "n", coef: 1 };
      });
      return coef;
    },

    resetButton() {
      this.coefficient = this.resetCoef();
    },
  },
  computed() {
    storeCoef(this.$store.getters.getParams);
  },
  created() {
    this.LOC = this.calculateLOC();
    if (this.$store.getters.getParams) {
      // this.LOC = this.$store.getters.getParams.LOC;
      this.coefficient = this.$store.getters.getParams.coefficient;
      this.currentTeam = this.$store.getters.getParams.currentTeam;
    } else {
      // this.LOC = this.calculateLOC();
      this.coefficient = this.resetCoef();
      this.$store.commit("setParams", { LOC: this.LOC, coefficient: this.coefficient, currentTeam: this.currentTeam });
    }
    this.updateResult();
  },
  watch: {
    currentTeam() {
      this.updateResult();
    },
    LOC() {
      this.updateResult();
    },
  },
};
</script>
