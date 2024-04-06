<template>
  <v-sheet>
    <v-container align="center">
      <h1>Statistics</h1>
    </v-container>
    <v-container align="center">
      <v-progress-circular v-if="loader" indeterminate size="50" width="10" color="grey-darken-3"></v-progress-circular>
    </v-container>
    <v-container v-if="showContent">
      <v-table>
        <thead>
          <tr>
            <th> Name </th>
            <th> Churn </th>
            <th> Overall </th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="churn, name in result.churn ">
            <td> {{ name }} </td>
            <td> {{ Math.round(result.churn[name]) }}% </td>
            <td> {{ result.overall[name] }} </td>
          </tr>
        </tbody>
      </v-table>
    </v-container>
  </v-sheet>
</template>
<script>
export default {
  props: {
    result: [],
    loader: false,
    showContent: false
  }
}
</script>