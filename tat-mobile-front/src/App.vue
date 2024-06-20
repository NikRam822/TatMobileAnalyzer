<template>
  <v-app>
    <v-app-bar>
      <v-app-bar-nav-icon
        v-show="this.$vuetify.display.xs || this.$vuetify.display.sm"
        @click="showdrawer = !showdrawer"
      ></v-app-bar-nav-icon>
      <v-btn variant="plain" to="/" title="Go dashboard" size="x-large">
        <v-icon size="x-large">
          <v-img src="./assets/logo.svg"></v-img>
        </v-icon>
      </v-btn>
      <v-app-bar-title
        v-show="!this.$vuetify.display.xs || this.$route.name == '/project-review'"
        style="color: rgb(197, 226, 21)"
        >{{ currentPage }}</v-app-bar-title
      >
      <v-spacer></v-spacer>
      <v-text-field
        v-if="this.$route.name == '/'"
        style="min-width: 200px"
        hide-details="true"
        density="compact"
        prepend-inner-icon="mdi-magnify"
        variant="outlined"
        class="mr-8"
        label="Search reposytory"
        v-model="searchRepo"
      ></v-text-field>
      <v-icon icon="mdi-account" size="40px" class="mr-10"></v-icon>
    </v-app-bar>
    <v-navigation-drawer v-if="currentPage != 'Dashboard'" width="400">
      <ListOfStats />
    </v-navigation-drawer>
    <v-navigation-drawer
      v-if="currentPage != 'Dashboard' && showdrawer && (this.$vuetify.display.xs || this.$vuetify.display.sm)"
      permanent
      width="400"
    >
      <ListOfStats />
    </v-navigation-drawer>
    <v-main style="width: 100%" class="d-flex">
      <v-sheet class="pa-4 ma-5 flex-fill" rounded="xl" elevation="4">
        <router-view :searchRepo="searchRepo" />
      </v-sheet>
    </v-main>
  </v-app>
</template>

<script>
export default {
  data() {
    return {
      searchRepo: "",
      showdrawer: false,
    };
  },
  methods: {},
  computed: {
    currentPage() {
      switch (this.$route.name) {
        case "/project-review":
          return this.$store.state.currentRepo.projectName;
        default:
          return "Dashboard";
      }
    },
  },
};
</script>
