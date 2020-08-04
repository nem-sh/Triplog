<template>
  <v-simple-table>
    <tbody>
      <tr>
        {{neighborNum}}
      </tr>
    </tbody>
  </v-simple-table>
</template>

<script>
export default {
  name: "neighborList",
  props: {
    userNum: { type: Number },
    neighborNum: { type: Number },
  },
}
</script>

<style>

</style>