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

    <v-container class="ma-2 d-flex flex-column justify-space-around" width=" 50%">
      <div class="d-flex flex-row justify-space-around">
        <div class="d-flex flex-column">
          <v-sheet class="text-center pa-2" style="border: solid, 1px, white; border-radius: 15px">
            <p style="color: rgb(197, 226, 21)">Person-months with risk</p>
            <v-chip size="x-large">
              {{ laborIntensityWithRisk }}
            </v-chip>
          </v-sheet>
          <v-sheet class="text-center">
            <p>Person-months</p>
            <v-chip size="x-large">{{ personMonths }} </v-chip>
          </v-sheet>
        </div>
        <div class="d-flex flex-row ga-3">
          <v-sheet class="text-center d-flex flex-column">
            <p>Months</p>
            <v-chip size="x-large" style="width: 100px" variant="outlined" @click="this.$refs.months.focus()">
              {{ Math.round(months) }}
            </v-chip>
            <div style="align-self: center">
              <v-text-field
                ref="months"
                density="compact"
                hide-spin-buttons
                hide-details
                type="number"
                variant="outlined"
                min="0"
                @input="updateResult('months')"
                v-model="months"
                style="width: 70px; border-top: 0px"
              ></v-text-field>
            </div>
          </v-sheet>
          <v-sheet class="text-center d-flex flex-column">
            <p>Personnel</p>
            <v-chip size="x-large" variant="outlined" style="width: 100px" @click="this.$refs.persons.focus()">
              {{ Math.round(personel) }}
            </v-chip>
            <div style="align-self: center">
              <v-text-field
                ref="persons"
                density="compact"
                hide-spin-buttons
                type="number"
                variant="outlined"
                min="0"
                @input="updateResult('personal')"
                style="width: 70px; border-top: 0px"
                v-model="personel"
                hide-details
              ></v-text-field>
            </div>
          </v-sheet>
        </div>
      </div>
    </v-container>
    <v-container width="50%" height="100%">
      <v-expansion-panels>
        <v-expansion-panel>
          <v-expansion-panel-title>Calculate cost</v-expansion-panel-title>
          <v-expansion-panel-text>
            <div class="d-flex flex-row justify-space-around">
              <div class="d-flex flex-row">
                <v-sheet class="text-center d-flex flex-column" title="Your cost of project">
                  <p>Your cost</p>
                  <v-text-field
                    type="number"
                    min="0"
                    hide-details
                    v-model="yourCost"
                    density="compact"
                    style="width: 120px"
                  ></v-text-field>
                </v-sheet>
                <v-icon icon="mdi-minus" class="align-self-end pb-4"></v-icon>
                <v-sheet class="text-center d-flex flex-column" title="Your pay for person">
                  <p>Pay for person</p>
                  <v-text-field
                    type="number"
                    min="0"
                    hide-details
                    v-model="pay"
                    density="compact"
                    style="width: 120px"
                  ></v-text-field>
                </v-sheet>
                <v-icon icon="mdi-arrow-right" class="align-self-end pb-4"></v-icon>
                <v-sheet class="text-center d-flex flex-column" title="labor intensity with risk * pay">
                  <p>Cost</p>
                  <v-chip size="large">{{ Math.round(pay * laborIntensityWithRisk) }}</v-chip>
                </v-sheet>
                <v-icon icon="mdi-equal" class="align-self-end pb-4"></v-icon>
                <v-sheet class="text-center d-flex flex-column" title="Your cost minus Pay">
                  <p>Difference</p>
                  <v-chip size="large">{{ yourCost - Math.round(pay * laborIntensityWithRisk) }}</v-chip>
                </v-sheet>
              </div>
            </div>
          </v-expansion-panel-text>
        </v-expansion-panel>
      </v-expansion-panels>
    </v-container>
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
              coefficient[param.codeName] = {
                simpleCoef: 'vl',
                coef: param.coefficient.vl,
              };
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
              coefficient[param.codeName] = {
                simpleCoef: 'l',
                coef: param.coefficient.l,
              };
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
              coefficient[param.codeName] = {
                simpleCoef: 'n',
                coef: param.coefficient.n,
              };
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
              coefficient[param.codeName] = {
                simpleCoef: 'h',
                coef: param.coefficient.h,
              };
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
              coefficient[param.codeName] = {
                simpleCoef: 'vh',
                coef: param.coefficient.vh,
              };
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
              :min="Math.min(...Object.values(param.coefficient))"
              :max="Math.max(...Object.values(param.coefficient))"
              step="0.01"
              append-icon="mdi-plus-circle"
              prepend-icon="mdi-minus-circle"
              @click:append="next(param.codeName, param.coefficient)"
              @click:prepend="prev(param.codeName, param.coefficient)"
              thumb-label
              hide-details
              :ticks="Object.fromEntries(Object.entries(param.coefficient).map(([key, value]) => [value, key]))"
              show-ticks="always"
              tick-size="6"
              v-model="coefficient[param.codeName].coef"
              @click="updateCoef(param.codeName, param.coefficient)"
            ></v-slider>
          </div>
        </td>
      </tr>
    </tbody>
  </v-table>
</template>
<script>
import axios from "axios";
import tableParams from "./table.ts";
let server_path = import.meta.env.VITE_BACKEND_URL;
export default {
  data() {
    return {
      table: tableParams,
      teamTeamType: ["ORGANIC", "SEMIDETACH", "EMBEDDED"],
      currentTeam: "ORGANIC",
      LOC: 0,
      coefficient: {},
      personMonths: 0,
      months: 0,
      editingMonths: 0,
      personel: 0,
      editingPersonel: 0,
      laborIntensityWithRisk: 0,
      modes: ["Go to extend mode", "Go to simple mode"],
      currentCoefMode: 0,
      yourCost: 0,
      pay: 0,
      costDiference: 0,
    };
  },
  methods: {
    next(name, coefTable) {
      const currCoef = this.coefficient[name].coef;
      const coefs = Object.values(coefTable).sort((a, b) => a - b);
      for (let value of coefs) {
        if (value > currCoef) {
          this.coefficient[name].coef = value;
          break;
        }
      }
    },
    prev(name, coefTable) {
      const currCoef = this.coefficient[name].coef;
      const coefs = Object.values(coefTable).sort((a, b) => a - b);
      for (let value of coefs.reverse()) {
        if (value < currCoef) {
          this.coefficient[name].coef = value;
          break;
        }
      }
    },
    updateCoef(name, coefTable) {
      const currCoef = this.coefficient[name].coef;
      const coefs = Object.entries(coefTable);
      let min = 1000;
      for (let index in coefs) {
        if (Math.abs(coefs[index][1] - currCoef) >= min) {
          this.coefficient[name].simpleCoef = coefs[index - 1][0];
          break;
        } else {
          min = Math.abs(coefs[index][1] - currCoef);
        }
        this.coefficient[name].simpleCoef = coefs[index][0];
      }
      this.updateResult();
    },

    async updateResult(option = null) {
      this.$store.commit("setParams", {
        LOC: this.LOC,
        coefficient: this.coefficient,
        currentTeam: this.currentTeam,
      });
      const payload = {};
      for (let item of this.table) {
        payload[item.codeName] = this.coefficient[item.codeName].coef;
      }
      payload.kloc = this.LOC;
      payload.projectType = this.currentTeam;
      switch (option) {
        case "months":
          if (!this.months) {
            return;
          }
          payload.months = this.months;
          break;
        case "personal":
          if (!this.personel) {
            return;
          }
          payload.personal = this.personel;
          break;
      }
      let hostadress = server_path + "/api/cocomo/calculate";
      try {
        let response = await axios.post(hostadress, payload);
        response = response.data;
        this.personMonths = response.personMonths;
        if (option !== "months") {
          this.months = response.months;
        }
        if (option !== "personal") {
          this.personel = response.personnel;
        }
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
      this.$store.commit("setParams", {
        LOC: this.LOC,
        coefficient: this.coefficient,
        currentTeam: this.currentTeam,
      });
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
