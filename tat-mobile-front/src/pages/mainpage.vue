<template>
  <v-layout>
    <v-app-bar>
      <v-img src="../assets/Logo.svg" height="40" max-width="64"></v-img>
      <v-app-bar-title class="text-lime-accent-3">Dashboard</v-app-bar-title>
      <v-spacer></v-spacer>
      <v-icon icon="mdi-account" size="40px" class="mr-10"></v-icon>
    </v-app-bar>
    <v-main>
      <v-sheet height="100%">
        <v-container>
          <v-row>
            <v-col v-for="val, url in this.$store.state.repositories" cols="auto">
              <v-card @click="navigateToOtherPage(url)" rounded="xl" height="150" width="400" border="md">
                <v-card-title class="ma-5">
                  <v-row>
                    {{ url.slice(url.lastIndexOf('/') + 1) }}
                    <v-spacer />
                    <v-btn flat @click.stop="this.$store.commit('delRepos', url)" icon="mdi-trash-can-outline">
                    </v-btn>
                  </v-row>
                </v-card-title>
                <v-card-subtitle class="ma-3">{{ url }}</v-card-subtitle>
              </v-card>
            </v-col>
            <v-col>

              <v-card @click="cardAppend = !cardAppend" v-if="cardAppend" rounded="xl" height="150" width="400"
                border="md">
                <v-col align="center">
                  <v-icon icon="mdi-plus-circle-outline" size="150" color="rgb(92, 99, 106)"></v-icon>
                </v-col>
              </v-card>

              <v-card v-else rounded="xl" height="150" width="400" border="md">
                <v-form @submit.prevent="addCard()">
                  <v-text-field required :rules="[re.test(rep) || 'Wrong URL']" v-model="rep"
                    label="Enter reposytory URL" type="url"></v-text-field>
                  <v-container v-show="loader" class="mt-2">
                    <v-row justify="center">
                      <v-progress-linear color="rgb(92, 99, 106)" height="6" indeterminate rounded></v-progress-linear>
                      <p> Analyzing reposytory </p>
                    </v-row>
                  </v-container>
                  <v-col v-show="!loader" align="center">
                    <v-btn elevation="2" :disabled="!rep" type="submit" icon="mdi-plus-circle-outline"></v-btn>
                  </v-col>
                </v-form>
              </v-card>

            </v-col>
          </v-row>
        </v-container>
      </v-sheet>
    </v-main>
  </v-layout>
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
  }),
  methods: {
    async addCard() {
      this.loader = true
      if (this.re.test(this.rep)) {
      } else {
        alert('URl should be: https://github.com/AUTOR/REPO')
      }
      let hostadress = "http://localhost:8080/patch/statistic"
      try {
        const response = await axios.post(hostadress, {
          repositoryUrl: this.rep,
        });
        this.result = response.data
        this.$store.commit('addRepos', [this.rep, this.result])
      } catch (error) {
        console.error('Error fetching repositories:', error);
      }
      this.rep = ''
      this.loader = false
    },
    navigateToOtherPage(repoName) {
      this.$router.push({ path: '/project-review', query: { repoName: repoName } });
    }
  }
}
</script>
