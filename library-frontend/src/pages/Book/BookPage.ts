import { ref } from 'vue';
import { type QTableColumn, type QTableProps } from 'quasar';
import type { BookType } from 'src/types/bookType';
import { deburr } from 'lodash';
import axios from 'axios';
import { notifyConfirm, notifyError, notifySuccess, notifyWarning } from 'src/services/messageService';
import { createBook, listAllBook, removeBook, updateBook } from 'src/services/bookService';
import type { GenreType } from 'src/types/genreType';
import type { AuthorType } from 'src/types/authorType';
import { listAllGenre } from 'src/services/genreService';
import { listAllAuthor } from 'src/services/authorService';

const ALREADY_EXISTS_CODES = [409, 'ALREADY_EXISTS'];

const form = ref<BookType>(createEmptyForm());

const items = ref<BookType[]>([]);
const validate = ref(false);
const editingIndex = ref<number | null>(null);
const showForm = ref(false);

const filter = ref('');

const authors = ref<AuthorType[]>([]);
const genres = ref<GenreType[]>([]);

const columns: QTableProps['columns'] = [
  {
    name: 'title',
    label: 'Título',
    field: row => row.title,
    align: 'left',
    sortable: true
  },
  {
    name: 'author.name',
    label: 'Autor',
    field: row => row.author.name,
    align: 'left',
    sortable: true
  },
  {
    name: 'genre.description',
    label: 'Gênero',
    field: row => row.genre.description,
    align: 'left',
    sortable: true
  },
  {
    name: 'createdAt',
    label: 'Data de Criação',
    field: row => row.createdAt ? new Date(row.createdAt).toLocaleDateString() : '',
    align: 'left',
    sortable: true
  },
  {
    name: 'actions',
    label: '',
    field: 'actions',
    align: 'right'
  }
];

const pagination = ref({
  page: 1,
  rowsPerPage: 10
});

function normalize(str: string): string {
  return deburr(str)
    .normalize('NFD')
    .replace(/[\u0300-\u036f]/g, '')
    .replace(/[^a-zA-Z0-9 ]/g, '')
    .toLowerCase()
    .trim();
}

function customFilterMethod(
  rows: readonly BookType[],
  rawFilter: string,
  columns: readonly QTableColumn[],
): readonly BookType[] {
  const normalizedFilter = normalize(rawFilter);

  return rows.filter((row) => {
    return columns.some((col) => {
      const value = typeof col.field === 'function'
        ? col.field(row)
        : (row as unknown as Record<string, unknown>)[col.field];
      return normalize(String(value ?? '')).includes(normalizedFilter);
    });
  });
}

function createEmptyForm(): BookType {
  return {
    id: null,
    title: null,
    author: null,
    genre: null,
    createdAt: null
  };
}

function clearForm() {
  form.value = createEmptyForm();
  validate.value = false;
  showForm.value = false;
}

async function loadItems() {
  const bookTypeList = await listAllBook();

  items.value = bookTypeList ?? [];
}

async function loadSelectOptions() {
  const authorTypeResponse = await listAllAuthor();
  authors.value = authorTypeResponse ?? [];

  const genreResponse = await listAllGenre();
  genres.value = genreResponse ?? [];
}

function openForm() {
  clearForm();
  editingIndex.value = null;
  showForm.value = true;
}

function selectItem(item: BookType, index: number) {
  editingIndex.value = index;
  validate.value = false;

  form.value = { ...item };
}

function selectItemWithDialog(item: BookType, index: number) {
  selectItem(item, index);
  showForm.value = true;
}

async function onSubmit() {
  validate.value = true;

  if (
    form.value.title &&
    form.value.author &&
    form.value.genre
  ) {
    try {
      if (form.value.id !== null) {
        await updateBook(form.value.id, form.value);

        notifySuccess('Livro salvo com sucesso!');
      } else {
        await createBook(form.value);
        notifySuccess('Livro criado com sucesso!');
      }

      await loadItems();
      clearForm();
      editingIndex.value = null;
      showForm.value = false;
    } catch (error: unknown) {
      if (axios.isAxiosError(error) && error.response?.data?.message && error.response.data.code) {
        const isDuplicate = ALREADY_EXISTS_CODES.includes(error.response.data.code);
        if (isDuplicate) {
          notifyWarning(error.response.data.message);
        } else {
          notifyError(error.response.data.message);
        }
      } else {
        notifyError('Erro desconhecido ao salvar o registro.');
      }
    }
  }
}

async function submitAndClose() {
  await onSubmit();
}

async function deleteItem(index: number) {
  const confirmed = await notifyConfirm('Deseja excluir este item?')

  if (confirmed) {
    const item = items.value[index];
    if (item?.id != null) {
      await removeBook(item.id);
      notifySuccess('Livro excluído com sucesso!');
      await loadItems();
    }
  }
}

function cancelEditAndClose() {
  clearForm();
  editingIndex.value = null;
  showForm.value = false;
}

export function useBookPage() {
  return {
    form,
    items,
    columns,
    pagination,
    filter,
    customFilterMethod,
    validate,
    editingIndex,
    showForm,
    openForm,
    selectItemWithDialog,
    deleteItem,
    cancelEditAndClose,
    submitAndClose,
    loadItems,
    loadSelectOptions,
    authors,
    genres
  };
}
