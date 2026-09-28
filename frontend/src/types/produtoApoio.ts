export type ProdutoStatus = 'ATIVO' | 'INATIVO';

export interface TipoProdutoDTO {
  id?: number;
  nome: string;
  descricao?: string;
  status?: ProdutoStatus;
}

export interface UnidadeMedidaDTO {
  id?: number;
  sigla: string;
  descricao: string;
  status?: ProdutoStatus;
}

export interface CategoriaDTO {
  id?: number;
  descricao: string;
  status?: ProdutoStatus;
}

export interface SubCategoriaDTO {
  id?: number;
  idCategoria?: number;
  idsCategorias?: number[];
  nomeCategoria?: string;
  nomesCategorias?: string[];
  descricao: string;
  status?: ProdutoStatus;
}

export interface ClasseDTO {
  id?: number;
  descricao: string;
  status?: ProdutoStatus;
}

export interface ModeloDTO {
  id?: number;
  descricao: string;
  status?: ProdutoStatus;
}

export interface MarcaDTO {
  id?: number;
  descricao: string;
  status?: ProdutoStatus;
}

export interface MilimetroDTO {
  id?: number;
  espessura: number;
  descricao: string;
  status?: ProdutoStatus;
}

export interface MedidaDTO {
  id?: number;
  descricao: string;
  largura?: number;
  altura?: number;
  status?: ProdutoStatus;
}

export interface ProdutoApoioCatalogoDTO {
  tiposProduto: TipoProdutoDTO[];
  unidadesMedida: UnidadeMedidaDTO[];
  categorias: CategoriaDTO[];
  subcategorias: SubCategoriaDTO[];
  classes: ClasseDTO[];
  modelos: ModeloDTO[];
  marcas: MarcaDTO[];
  milimetros: MilimetroDTO[];
  medidas: MedidaDTO[];
}

export type TipoEntidadeApoio =
  | 'tipo_produto'
  | 'categoria'
  | 'subcategoria'
  | 'classe'
  | 'modelo'
  | 'marca'
  | 'unidade_medida'
  | 'medida'
  | 'milimetro';
