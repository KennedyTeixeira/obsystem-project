export type ClienteStatus = 'ATIVO' | 'INATIVO' | 'BLOQUEADO' | 'PENDENTE_PAGAMENTO';
export type FornecedorStatus = 'ATIVO' | 'INATIVO' | 'BLOQUEADO';
export type Genero = 'MASCULINO' | 'FEMININO' | 'OUTRO';
export type TipoContato = 'CELULAR' | 'WHATSAPP' | 'TELEFONE_FIXO';
export type TipoPagamento = 'BOLETO' | 'CHEQUE' | 'CREDIARIO' | 'DEPOSITO' | 'DINHEIRO' | 'PIX' | 'CARTAO' | 'VALE_TROCA';

export interface ClienteDTO {
  id?: number;
  idPessoa?: number;
  nome: string;
  tipoPessoa: 'F' | 'J';
  email?: string;
  segmento?: string;
  site?: string;
  observacao?: string;

  // Pessoa Física
  cpf?: string;
  rg?: string;
  genero?: Genero;

  // Pessoa Jurídica
  cnpj?: string;
  fantasia?: string;
  ie?: string;
  im?: string;

  // Dados do Cliente
  status?: ClienteStatus;
  saldo?: number;
  limite?: number;

  // Endereço rápido
  cep?: string;
  endereco?: string;
  numero?: string;
  complemento?: string;
  bairro?: string;
  cidade?: string;
  uf?: string;

  // Contato rápido
  telefone?: string;
  tipoContato?: TipoContato;
}

export interface FornecedorDTO {
  id?: number;
  idPessoa?: number;
  nome: string;
  tipoPessoa: 'F' | 'J';
  email?: string;
  segmento?: string;
  site?: string;
  observacao?: string;

  // Pessoa Física
  cpf?: string;
  rg?: string;
  genero?: Genero;

  // Pessoa Jurídica
  cnpj?: string;
  fantasia?: string;
  ie?: string;
  im?: string;

  // Dados do Fornecedor
  status?: FornecedorStatus;
  credito?: number;
  pagamento?: TipoPagamento;

  // Endereço rápido
  cep?: string;
  endereco?: string;
  numero?: string;
  complemento?: string;
  bairro?: string;
  cidade?: string;
  uf?: string;

  // Contato rápido
  telefone?: string;
  tipoContato?: TipoContato;
}
