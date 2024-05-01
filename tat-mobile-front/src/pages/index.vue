<template>
  <v-container fluid>
    <v-row>
      <!-- Карточка для тестов -->
      <v-col cols="auto">
        <v-card @click="testStats()" title="test" rounded="xl" height="165" width="400" border="md">
        </v-card>
      </v-col>

      <v-col v-for="rep in this.$store.state.repositories" cols="auto">
        <v-card @click="navigateToProjectReview(rep)" :title="rep.projectName" :subtitle="rep.projectLink" rounded="xl"
          height="165" width="400" border="md">
          <template v-slot:append>
            <v-btn @click.stop="cleanStats" flat icon="mdi-trash-can-outline"></v-btn>
          </template>
          <v-container v-show="loaderStats.includes(rep.projectLink)">
            <v-progress-linear color="rgb(92, 99, 106)" height="6" indeterminate rounded></v-progress-linear>
            <p> Analyzing reposytory </p>
          </v-container>
        </v-card>
      </v-col>
      <v-col cols="auto">

        <v-card @click="cardAppend = !cardAppend" v-if="cardAppend" rounded="xl" height="165" width="400" border="md"
          class="d-flex justify-center">
          <v-icon icon="mdi-plus-circle-outline" size="130" color="rgb(92, 99, 106)" class="align-self-center"></v-icon>
        </v-card>

        <v-card v-else rounded="xl" height="165" width="400" border="md">
          <v-form @submit.prevent="addCard()" class="d-flex flex-column">
            <v-text-field required :rules="[re.test(rep) || 'Wrong URL']" v-model="rep" label="Enter reposytory URL"
              type="url"></v-text-field>
            <v-container v-show="loader" class="mt-2">
              <v-row justify="center">
                <v-progress-linear color="rgb(92, 99, 106)" height="6" indeterminate rounded></v-progress-linear>
                <p> Adding reposytory </p>
              </v-row>
            </v-container>
            <v-btn v-show="!loader" elevation="2" :disabled="!rep" type="submit" icon="mdi-plus-circle-outline"
              class="align-self-center"></v-btn>
          </v-form>
        </v-card>

      </v-col>
    </v-row>
  </v-container>
</template>
<script>
import axios from 'axios'
export default {
  data: () => ({
    re: new RegExp("^https://github.com/([^/]+)/([^/]+)$"),
    cardAppend: true,
    rep: '',
    result: [],
    loader: false,
    loaderStats: [],
  }),
  methods: {
    // функция для тестовой карточки
    testStats() {
      this.$store.commit("changeCurrentRepo", { projectLink: 'test' });
      this.$store.commit("addStatistc", ["test", {
        "data": {
          "general": {
            "FirststFile": { "Olmecsandr": 7, "Nikita Ramzin": 4 },
          },
          "churn": {
            "Olmecsandr0": 50, "Olmecsandr1": 50, "Olmecsandr2": 50, "Olmecsandr3": 50, "Olmecsandr4": 50, "Olmecsandr5": 50, "Olmecsandr6": 50, "Olmecsandr7": 50, "Olmecsandr8": 50, "Olmecsandr9": 50, "Olmecsandr10": 50, "Olmecsandr11": 50, "Olmecsandr12": 50, "Olmecsandr13": 50, "Olmecsandr14": 50, "Olmecsandr15": 50, "Olmecsandr16": 50
          },
          "overall": { "Olmecsandr0": 100, "Olmecsandr1": 100, "Olmecsandr2": 100, "Olmecsandr3": 100, "Olmecsandr4": 100, "Olmecsandr5": 100, "Olmecsandr6": 100, "Olmecsandr7": 100, "Olmecsandr8": 100, "Olmecsandr9": 100, "Olmecsandr10": 100, "Olmecsandr11": 100, "Olmecsandr12": 100, "Olmecsandr13": 100, "Olmecsandr14": 100, "Olmecsandr15": 100, "Olmecsandr16": 100, }
        }
      }]),

        this.$router.push('/project-review')
    },
    async addCard() {
      this.loader = true
      if (this.re.test(this.rep)) {
      } else {
        alert('URl should be: https://github.com/AUTOR/REPO')
      }
      let hostadress = "http://localhost:8080/project/create"
      try {
        await axios.post(hostadress, {
          projectId: 0,
          projectLink: this.rep,
          projectName: this.rep.slice(this.rep.lastIndexOf('/') + 1)
        });
      } catch (error) {
        alert("This repo already exists")
        console.error('Error fetching repositories:', error);
      }
      this.$emit('get-repos')
      this.rep = ''
      this.cardAppend = true
      this.loader = false
    },
    async navigateToProjectReview(repo) {
      if (!this.$store.state.RepoSatistic[repo.projectLink]) {
        this.loaderStats.push(repo.projectLink)
        console.log(this.loaderStats)
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
          this.loaderStats.splice(this.loaderStats.indexOf(repo.projectLink), 1)
          return
        }
        this.loaderStats.splice(this.loaderStats.indexOf(repo.projectLink), 1)
        console.log(this.loaderStats)
      }
      else {
        this.$store.commit("changeCurrentRepo", repo);
        this.$router.push('/project-review')
      }
    },
    cleanStats() {
      this.$store.commit("deleteStatistic")
    }
  }
}
</script>
