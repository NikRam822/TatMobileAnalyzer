<template>
  <v-layout>
    <!-- Нужно закрепить app-bar -->
    <v-app-bar>
      <!-- Нужно переделать, пока что обошел с max-width -->
      <v-img src="../assets/Logo.svg" height="40" max-width="64"></v-img>
      <v-app-bar-title class="text-lime-accent-3">Dashboard</v-app-bar-title>
      <v-spacer></v-spacer>
      <v-icon icon="mdi-account" size="40px" class="mr-10"></v-icon>
    </v-app-bar>
    <v-main>
      <!-- Пока что использую v-sheet, хоть и не по назначению, за то удобно -->
      <v-sheet class="pa-6 ma-4" rounded="xl" elevation="4">
        <v-container fluid="true">
          <v-row>
            <v-col v-for="val, url in this.$store.state.repositories" cols="auto">
              <v-card rounded="xl" height="200" width="400" border="md">
                <v-card-subtitle>{{ url }}</v-card-subtitle>
                <v-btn @click="navigateToOtherPage(url)" class="ma-5">See more</v-btn>
              </v-card>
            </v-col>
            <v-col>
              <v-card @click="cardAppend = !cardAppend" v-if="cardAppend" rounded="xl" height="200" width="400"
                border="md">
                <!-- Надо поставить плюс в середину-->
                <v-col align="center">
                  <v-icon icon="mdi-plus-circle-outline" size="150" color="rgb(92, 99, 106)"></v-icon>
                </v-col>
              </v-card>
              <v-card v-else rounded="xl" height="200" width="400" border="md">
                <v-form @submit.prevent="addCard()">
                  <v-text-field required :rules="[re.test(rep) || 'Wrong URL']" v-model="rep"
                    label="Enter reposytory URL" type="url"></v-text-field>
                  <v-col align="center">
                    <v-btn :disabled="!rep" type="submit" icon="mdi-plus-circle-outline"></v-btn>
                  </v-col>
                </v-form>
                <v-progress-circular v-if="loader" indeterminate size="50" width="10"
                  color="grey-darken-3"></v-progress-circular>
              </v-card>
            </v-col>
          </v-row>
        </v-container>
      </v-sheet>
      <!-- <v-row>
                <v-col>
                    <Search @get-result="getRepos" />
                </v-col>
                <v-col>
                    <Content :result="result" :loader="loader" :showContent="showContent" />
                </v-col>
            </v-row> -->
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
    // showContent: false
  }),
  methods: {
    addCard() {
      if (this.re.test(this.rep)) {
        this.getRepos()
      } else {
        alert('URl should be: https://github.com/AUTOR/REPO')
      }
    },
    async getRepos() {
      let hostadress = "http://localhost:8080/patch/statistic"
      this.loader = true
      try {
        const response = await axios.post(hostadress, {
          repositoryUrl: this.rep,
        });
        this.result = response.data
        this.$store.commit('addRepos', [this.rep, this.result])
      } catch (error) {
        console.error('Error fetching repositories:', error);
      }
      this.loader = false
    },
    navigateToOtherPage(repoName) {
      this.$router.push({ path: '/project-review', query: { repoName: repoName } });
    }
  }
}
</script>
