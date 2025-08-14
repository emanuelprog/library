import { useRouter } from 'vue-router';
import { computed } from 'vue';
import { useUserStore } from 'src/stores/userStore';

export function useHomePage() {
  const router = useRouter();
  const userStore = useUserStore();

  const allCards = [
    {
      icon: 'category',
      title: 'Gêneros',
      description: 'Gerencie os gêneros disponíveis na livraria.',
      route: 'generos',
      roles: ['ROLE_WRITER']
    },
    {
      icon: 'person',
      title: 'Autores',
      description: 'Gerencie e consulte os autores cadastrados.',
      route: 'autores',
      roles: ['ROLE_WRITER']
    },
    {
      icon: 'menu_book',
      title: 'Livros',
      description:
        userStore.selectedUser?.role === 'ROLE_WRITER'
          ? 'Gerencie e consulte os livros cadastrados.'
          : 'Consulte os livros cadastrados.',
      route: 'livros',
      roles: ['ROLE_READER', 'ROLE_WRITER']
    }
  ];

  const cards = computed(() => {
    const role = userStore.selectedUser?.role;

    if (!role) return [];

    return allCards.filter(card => card.roles.includes(role));
  });

  function goTo(route: string) {
    void router.push(route);
  }

  return {
    goTo,
    cards
  };
}
