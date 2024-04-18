<template>
    <v-layout>
      <v-app-bar>
        <v-app-bar-nav-icon color="lime-accent-3">
        </v-app-bar-nav-icon>
        <v-app-bar-title class="text-lime-accent-3">Dashboard</v-app-bar-title>
      </v-app-bar>
      <v-main>
        <div class="ma-5">
          <v-row>
              <v-col>
                    <Search @get-result="getRepos" />
              </v-col>
              <v-col>
                    <Content :result="result" :loader="loader" :showContent="showContent" />
              </v-col>
          </v-row>
        </div>
      </v-main>
    </v-layout>
</template>

<script>
import axios from 'axios'
import dotenv from 'dotenv'

dotenv.config()
export default {
    data: () => ({
        result: [],
        loader: false,
        showContent: false
    }),
    methods: {
        async getRepos(repo, start, end) {
            let hostadress = process.env.VUE_APP_HOST_ADDRESS;
            this.loader = true
            this.showContent = false
            if (start && end) {
                hostadress += "?since=" + start + "&until=" + end
            } else if (start) {
                hostadress += "?since=" + start
            } else if (end) {
                hostadress += "?until=" + end
            }

            try {
                const response = await axios.post(hostadress, {
                    repositoryUrl: repo,
                });
                this.result = response.data;
            } catch (error) {
                console.error('Error fetching repositories:', error);
            }
            this.loader = false
            this.showContent = true
        }
    }
}
</script>
