<template>
  <q-page class="q-pa-md" :class="$q.dark.isActive ? 'bg-grey-10' : 'bg-white'">
    <div class="column items-center justify-center">
      <div class="row justify-center q-gutter-lg q-mt-md">
        <q-card v-for="(item, index) in cards" :key="index" class="card-functionality">
          <q-card-section class="column items-center q-pa-xl">
            <q-icon :name="item.icon" size="48px" color="primary" class="q-mb-md" />
            <div class="text-h6 text-center">{{ item.title }}</div>
            <div class="text-body2 text-center q-my-md">
              {{ item.description }}
            </div>
            <q-btn label="ACESSAR" color="primary" @click="goTo(item.route)" />
          </q-card-section>
        </q-card>
      </div>
    </div>
  </q-page>
</template>

<script setup lang="ts">
import { useHomePage } from './HomePage';
import './HomePage.scss';

const {
  goTo,
  cards,
} = useHomePage();
</script>
