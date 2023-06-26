import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IProcessInstances } from '../process-instances.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../process-instances.test-samples';

import { ProcessInstancesService } from './process-instances.service';

const requireRestSample: IProcessInstances = {
  ...sampleWithRequiredData,
};

describe('ProcessInstances Service', () => {
  let service: ProcessInstancesService;
  let httpMock: HttpTestingController;
  let expectedResult: IProcessInstances | IProcessInstances[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ProcessInstancesService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a ProcessInstances', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const processInstances = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(processInstances).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ProcessInstances', () => {
      const processInstances = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(processInstances).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ProcessInstances', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ProcessInstances', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ProcessInstances', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addProcessInstancesToCollectionIfMissing', () => {
      it('should add a ProcessInstances to an empty array', () => {
        const processInstances: IProcessInstances = sampleWithRequiredData;
        expectedResult = service.addProcessInstancesToCollectionIfMissing([], processInstances);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(processInstances);
      });

      it('should not add a ProcessInstances to an array that contains it', () => {
        const processInstances: IProcessInstances = sampleWithRequiredData;
        const processInstancesCollection: IProcessInstances[] = [
          {
            ...processInstances,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addProcessInstancesToCollectionIfMissing(processInstancesCollection, processInstances);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ProcessInstances to an array that doesn't contain it", () => {
        const processInstances: IProcessInstances = sampleWithRequiredData;
        const processInstancesCollection: IProcessInstances[] = [sampleWithPartialData];
        expectedResult = service.addProcessInstancesToCollectionIfMissing(processInstancesCollection, processInstances);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(processInstances);
      });

      it('should add only unique ProcessInstances to an array', () => {
        const processInstancesArray: IProcessInstances[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const processInstancesCollection: IProcessInstances[] = [sampleWithRequiredData];
        expectedResult = service.addProcessInstancesToCollectionIfMissing(processInstancesCollection, ...processInstancesArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const processInstances: IProcessInstances = sampleWithRequiredData;
        const processInstances2: IProcessInstances = sampleWithPartialData;
        expectedResult = service.addProcessInstancesToCollectionIfMissing([], processInstances, processInstances2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(processInstances);
        expect(expectedResult).toContain(processInstances2);
      });

      it('should accept null and undefined values', () => {
        const processInstances: IProcessInstances = sampleWithRequiredData;
        expectedResult = service.addProcessInstancesToCollectionIfMissing([], null, processInstances, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(processInstances);
      });

      it('should return initial array if no ProcessInstances is added', () => {
        const processInstancesCollection: IProcessInstances[] = [sampleWithRequiredData];
        expectedResult = service.addProcessInstancesToCollectionIfMissing(processInstancesCollection, undefined, null);
        expect(expectedResult).toEqual(processInstancesCollection);
      });
    });

    describe('compareProcessInstances', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareProcessInstances(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareProcessInstances(entity1, entity2);
        const compareResult2 = service.compareProcessInstances(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareProcessInstances(entity1, entity2);
        const compareResult2 = service.compareProcessInstances(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareProcessInstances(entity1, entity2);
        const compareResult2 = service.compareProcessInstances(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
