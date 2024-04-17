<template>
    <v-layout>
      <v-app-bar>
        <v-app-bar-nav-icon color="lime-accent-3">
        </v-app-bar-nav-icon>
        <v-app-bar-title class="text-lime-accent-3">Dashboard</v-app-bar-title>
      </v-app-bar>
      <v-main>
        <v-row>
            <v-col>
                <Search @get-result="getRepos" />
            </v-col>
            <v-col>
                <Content :result="result" :loader="loader" :showContent="showContent" />
            </v-col>
        </v-row>
      </v-main>
    </v-layout>
</template>

<script>
import axios from 'axios'
export default {
    data: () => ({
        result: [],
        loader: false,
        showContent: false
    }),
    methods: {
        async getRepos(repo, start, end) {
            let hostadress = "http://localhost:8080/patch/statistic"
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
