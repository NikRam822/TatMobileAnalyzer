<template>
    <v-row>
        <v-col>
            <v-sheet>
                <Search @get-result="getRepos"/>
            </v-sheet>
        </v-col>
        <v-col>
            <v-sheet>
                <Content :result="result" :loader="loader"/>
            </v-sheet>
        </v-col>
    </v-row>
</template>

<script>
import axios from 'axios'
export default {
    data: () => ({
        result: [],
        loader:false
    }),
    methods: {
        async getRepos(repo, start, end) {
            let hostadress = "http://localhost:8080/patch/statistic"
            this.loader = true
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
        }
    }
}
</script>