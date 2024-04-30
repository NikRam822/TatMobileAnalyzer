<template>
    <v-table>
        <thead>
            <tr>
                <th> Name </th>
                <th> Overall </th>
                <th> Churn </th>
                <th> Total </th>
            </tr>
        </thead>
        <tbody>
            <tr v-for="autor in this.$store.state.stats">
                <td>{{ autor.name }}</td>
                <td>{{ autor.overall }}</td>
                <td>{{ autor.churn }}</td>
                <td>{{ autor.total }}</td>
            </tr>
        </tbody>
    </v-table>
</template>
<script>
export default {
}
</script>