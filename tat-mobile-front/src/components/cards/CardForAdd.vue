<template>
  <v-col xs="12" sm="5" md="4" lg="3" xl="2">
    <v-card
      @click="cardAppend = !cardAppend"
      v-if="cardAppend"
      rounded="xl"
      height="165"
      border="md"
      class="d-flex justify-center flex-1-0"
      style="max-width: 500px; min-width: 200px"
    >
      <v-icon icon="mdi-plus-circle-outline" size="130" color="rgb(92, 99, 106)" class="align-self-center"></v-icon>
    </v-card>
    <v-card v-else rounded="xl" height="165" border="md" class="flex-1-0" style="max-width: 500px; min-width: 200px">
      <v-form @submit.prevent="addCard()" class="d-flex flex-column">
        <v-text-field
          required
          :rules="[re.test(rep) || 'Wrong URL']"
          v-model="rep"
          label="Enter reposytory URL"
          type="url"
        ></v-text-field>
        <v-btn
          elevation="2"
          :disabled="!rep"
          type="submit"
          icon="mdi-plus-circle-outline"
          class="align-self-center"
        ></v-btn>
      </v-form>
    </v-card>
  </v-col>
</template>
<script>
import axios from "axios";
export default {
  data() {
    return {
      rep: "",
      cardAppend: true,
      re: new RegExp("^https://github.com/([^/]+)/([^/]+)$"),
    };
  },
  methods: {
    async addCard() {
      // if (this.re.test(this.rep)) {
      let hostadress = "http://localhost:8080/project/create";
      try {
        await axios.post(hostadress, {
          projectId: 0,
          projectLink: this.rep,
          projectName: this.rep.slice(this.rep.lastIndexOf("/") + 1),
        });
      } catch (error) {
        alert("This repo already exists");
        console.error("Error fetching repositories:", error);
      }
      // } else {
      //   alert("URl should be: https://github.com/author/REPO");
      // }
      this.$emit("get-repos");
      this.rep = "";
      this.cardAppend = true;
    },
  },
};
</script>
